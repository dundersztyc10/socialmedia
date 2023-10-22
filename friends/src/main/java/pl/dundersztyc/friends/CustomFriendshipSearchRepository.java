package pl.dundersztyc.friends;

import java.util.List;

interface CustomFriendshipSearchRepository {
    List<Account> findFriendsOfAccountIdWithDepth(String accountId, int depth);
}
