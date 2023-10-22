package pl.dundersztyc.friends;

import java.util.List;
import java.util.Optional;

interface FriendshipRepository {
    Account save(Account account);
    List<Account> findFriendsOfAccountId(String accountId);
    List<Account> findFriendsOfAccountIdWithDepth(String accountId, int depth);
    Optional<Account> findByAccountId(String accountId);
    List<Account> findAll(); // TODO: delete
    void deleteAll(); // TODO: delete
}
