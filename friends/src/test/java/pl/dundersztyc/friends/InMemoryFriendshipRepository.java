package pl.dundersztyc.friends;


import lombok.SneakyThrows;
import org.apache.http.MethodNotSupportedException;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @SneakyThrows
    @Override
    // TODO: create in-memory neo4j
    public List<Account> findFriendsOfAccountIdWithDepth(String accountId, int depth) {
        throw new MethodNotSupportedException("");
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
}
