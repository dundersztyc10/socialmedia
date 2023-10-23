package pl.dundersztyc.friends;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dundersztyc.common.events.EventPublisher;
import pl.dundersztyc.friends.dto.AccountDto;
import pl.dundersztyc.friends.dto.Depth;
import pl.dundersztyc.friends.dto.FriendshipRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FriendshipQueryRepositoryTest {

    private FriendshipQueryRepository friendshipQueryRepo;
    private FriendshipFacade friendshipFacade;

    @BeforeEach
    void setUp() {
        var repository = new InMemoryFriendshipRepository();
        EventPublisher eventPublisher = mock(EventPublisher.class);
        friendshipQueryRepo = new FriendsConfiguration().friendshipQueryRepository(
                repository);
        friendshipFacade = new FriendsConfiguration().friendshipFacade(
                repository, eventPublisher);
    }

    @Test
    void shouldFindByAccountId() {
        addFriendship("from", "to");

        var accountDto = friendshipQueryRepo.findByAccountId("from");

        assertThat(accountDto.accountId()).isEqualTo("from");
    }

    @Test
    void shouldThrowWhenFindAndAccountDoesNotExist() {
        assertThrows(EntityNotFoundException.class,
                () -> friendshipQueryRepo.findByAccountId("idDoesNotExist"));
    }

    @Test
    void shouldFindFriendsOfAccount() {
        addFriendship("A", "B");
        addFriendship("B", "C");
        addFriendship("C", "A");
        addFriendship("C", "D");

        var friends = friendshipQueryRepo.findFriendsOfAccount("A");

        assertThat(friends).hasSize(2);
        assertThat(friends.stream().map(AccountDto::accountId).collect(Collectors.toSet()))
                .isEqualTo(Set.of("B", "C"));
    }

    @Test
    void shouldFindFriendsOfAccountWithDepth() {
        addFriendship("A", "B");
        addFriendship("B", "C");
        addFriendship("C", "A");
        addFriendship("C", "D");

        var friends = friendshipQueryRepo.findFriendsOfAccountWithDepth("A", new Depth(2));

        assertThat(friends).hasSize(3);
        assertThat(friends.stream().map(AccountDto::accountId).collect(Collectors.toSet()))
                .isEqualTo(Set.of("B", "C", "D"));
    }

    @Test
    void shouldThrowWhenDepthIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> friendshipQueryRepo.findFriendsOfAccountWithDepth("A", new Depth(-1)));
    }

    private void addFriendship(String idFrom, String idTo) {
        friendshipFacade.addFriendship(new FriendshipRequest(idFrom, idTo));
    }

}