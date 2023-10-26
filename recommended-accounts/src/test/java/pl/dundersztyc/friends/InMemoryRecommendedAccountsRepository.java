package pl.dundersztyc.friends;

import jakarta.persistence.EntityNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class InMemoryRecommendedAccountsRepository implements RecommendedAccountsRepository {

    private ConcurrentHashMap<String, Account> friendships = new ConcurrentHashMap<>();

    @Override
    public List<Account> findRecommendedAccounts(String accountId, int limit) {
        var unsorted = findFriendsOfAccountIdWithDepth(accountId, 2);
        return unsorted.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(a -> a.getValue().get())))
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
    }

    void addFriendship(String id1, String id2) {
        var account1 = findByAccountId(id1)
                .orElseGet(() -> save(new Account(id1)));
        var account2 = findByAccountId(id2)
                .orElseGet(() -> save(new Account(id2)));
        account1.addFriend(account2);
        account2.addFriend(account1);
        save(account1);
        save(account2);
    }

    private Account save(Account account) {
        friendships.put(account.getAccountId(), account);
        return account;
    }

    private Map<Account, AtomicInteger> findFriendsOfAccountIdWithDepth(String accountId, int depth) {
        // dirty DFS implementation
        var account = findByAccountId(accountId).orElseThrow(EntityNotFoundException::new);

        Set<Account> uniqueFriends = new HashSet<>();
        Map<Account, AtomicInteger> friends = new HashMap<>();
        Queue<Account> queue = new LinkedList<>();
        Set<Account> visited = new HashSet<>();

        queue.add(account);
        visited.add(account);

        int currentDepth = 0;

        while (!queue.isEmpty() && currentDepth < depth) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                Account currentAccount = queue.poll();

                for (var friend : currentAccount.getFriends()) {
                    friends.putIfAbsent(friend, new AtomicInteger(0));
                    friends.get(friend).incrementAndGet();
                }

                for (Account friend : currentAccount.getFriends()) {
                    if (!visited.contains(friend)) {
                        queue.add(friend);
                        visited.add(friend);
                    }
                }
            }

            currentDepth++;
        }
        friends.remove(account);
        for (var depth1friends : account.getFriends()) {
            friends.remove(depth1friends);
        }
        return friends;
    }

    private Optional<Account> findByAccountId(String accountId) {
        return friendships.values().stream()
                .filter(friendship -> friendship.getAccountId().equals(accountId))
                .findFirst();
    }
}
