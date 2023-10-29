package pl.dundersztyc.friendsnearby;

import lombok.RequiredArgsConstructor;
import pl.dundersztyc.friends.FriendshipQueryRepository;
import pl.dundersztyc.friends.dto.AccountDto;
import pl.dundersztyc.friendsnearby.dto.FriendsNearbyDto;
import pl.dundersztyc.friendsnearby.dto.FriendNearby;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AccountLocationQueryRepository {

    private final AccountLocationRepository accountLocationRepo;
    private final FriendshipQueryRepository friendshipQueryRepo;
    private final Clock clock;

    final static int MAX_MINUTES_SINCE_LAST_ACCOUNT_RECORD = 10;
    private final static double MAX_DISTANCE_IN_METERS = 1000.0;

    public FriendsNearbyDto findFriendsNearby(String accountId) {
        LocalDateTime initialSearchDate = initialSearchDate();
        Optional<AccountLocation> lastAccountRecord =
                accountLocationRepo.findFirstByAccountIdAndDateGreaterThanEqualOrderByDateDesc(accountId, initialSearchDate);
        if (lastAccountRecord.isEmpty()) {
            return new FriendsNearbyDto(accountId, Collections.emptyList());
        }

        var friendsIds = friendshipQueryRepo.findFriendsOfAccount(accountId)
                .stream()
                .map(AccountDto::accountId)
                .collect(Collectors.toList());

        List<AccountLocation> validLatestFriendRecords =
                accountLocationRepo.findLatestFriendsRecordsSince(friendsIds, initialSearchDate);

        List<FriendNearby> friendsNearby =
                accountLocationRepo.findFriendsNearby(lastAccountRecord.get().getLocation(), validLatestFriendRecords, MAX_DISTANCE_IN_METERS);

        return new FriendsNearbyDto(accountId, friendsNearby);
    }


    private LocalDateTime initialSearchDate() {
        return LocalDateTime.now(clock).minusMinutes(MAX_MINUTES_SINCE_LAST_ACCOUNT_RECORD);
    }

}
