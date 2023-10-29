package pl.dundersztyc.friendsnearby;

import org.locationtech.jts.geom.Point;
import pl.dundersztyc.friendsnearby.dto.FriendNearby;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

interface AccountLocationRepository {
    AccountLocation save(AccountLocation accountLocation);
    List<AccountLocation> findLatestFriendsRecordsSince(List<String> accountIds, LocalDateTime since);
    Optional<AccountLocation> findFirstByAccountIdAndDateGreaterThanEqualOrderByDateDesc(String accountId, LocalDateTime dateTime);
    List<FriendNearby> findFriendsNearby(Point accountLocation, List<AccountLocation> friendRecords, double maxDistanceInMeters);
}
