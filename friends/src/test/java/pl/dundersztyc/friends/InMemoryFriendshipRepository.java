package pl.dundersztyc.friends;


import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.apache.http.MethodNotSupportedException;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

class InMemoryFriendshipRepository implements FriendshipRepository {

    private ConcurrentHashMap<String, Account> friendships = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        friendships.put(account.getAccountId(), account);
        return account;
    }

    @Override
    public List<Account> findFriendsOfAccountId(String accountId) {
        return new ArrayList<>(
                friendships.values().stream()
                        .filter(friendship -> friendship.getAccountId().equals(accountId))
                        .findFirst()
                        .orElseThrow()
                        .getFriends()
        );
    }

    @Override
    public Optional<Account> findByAccountId(String accountId) {
        return friendships.values().stream()
                .filter(friendship -> friendship.getAccountId().equals(accountId))
                .findFirst();
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(friendships.values());
    }

    @Override
    public void deleteAll() {
        friendships.clear();
    }


    @Override
    public List<Account> findFriendsOfAccountIdWithDepth(String accountId, int depth) {
        // DFS implementation
        var account = findByAccountId(accountId).orElseThrow(EntityNotFoundException::new);

        Set<Account> uniqueFriends = new HashSet<>();
        Queue<Account> queue = new LinkedList<>();
        Set<Account> visited = new HashSet<>();

        queue.add(account);
        visited.add(account);

        int currentDepth = 0;

        while (!queue.isEmpty() && currentDepth < depth) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                Account currentAccount = queue.poll();
                uniqueFriends.addAll(currentAccount.getFriends());

                for (Account friend : currentAccount.getFriends()) {
                    if (!visited.contains(friend)) {
                        queue.add(friend);
                        visited.add(friend);
                    }
                }
            }

            currentDepth++;
        }
        uniqueFriends.remove(account);
        return new ArrayList<>(uniqueFriends);
    }
}
