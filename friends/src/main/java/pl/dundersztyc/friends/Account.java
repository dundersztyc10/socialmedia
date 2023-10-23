package pl.dundersztyc.friends;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Node
@Getter
class Account {

    public Account(String accountId) {
        this.accountId = accountId;
        this.friends = new HashSet<>();
    }

    @Id
    @GeneratedValue
    private Long id;

    private final String accountId;

    @Relationship(value = "FRIEND_OF")
    private Set<Account> friends;

    public void addFriend(@NonNull Account friend) {
        friends.add(friend);
    }

    public void deleteFriend(@NonNull Account friend) {
        friends.removeIf(elem -> Objects.equals(elem.id, friend.getId()));
    }

    public Set<Account> getFriends() {
        return Collections.unmodifiableSet(friends);
    }
}
