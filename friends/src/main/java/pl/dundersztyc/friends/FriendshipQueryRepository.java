package pl.dundersztyc.friends;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import pl.dundersztyc.friends.dto.AccountDto;
import pl.dundersztyc.friends.dto.Depth;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FriendshipQueryRepository {

    private final FriendshipRepository friendshipRepository;

    public AccountDto findByAccountId(String accountId) {
        return friendshipRepository.findByAccountId(accountId)
                .map(FriendshipQueryRepository::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<AccountDto> findFriendsOfAccount(String accountId) {
        return friendshipRepository.findFriendsOfAccountId(accountId)
                .stream()
                .map(FriendshipQueryRepository::toDto)
                .collect(Collectors.toList());
    }

    public List<AccountDto> findFriendsOfAccountWithDepth(String accountId, Depth depth) {
        return friendshipRepository.findFriendsOfAccountIdWithDepth(accountId, depth.value())
                .stream()
                .map(account -> new AccountDto(account.getAccountId()))
                .collect(Collectors.toList());
    }

    private static AccountDto toDto(Account account) {
        return new AccountDto(account.getAccountId());
    }

}
