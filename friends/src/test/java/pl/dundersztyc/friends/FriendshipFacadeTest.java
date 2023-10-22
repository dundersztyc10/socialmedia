package pl.dundersztyc.friends;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dundersztyc.common.events.EventPublisher;
import pl.dundersztyc.friends.dto.FriendshipDto;
import pl.dundersztyc.friends.dto.FriendshipExistException;
import pl.dundersztyc.friends.dto.FriendshipRequest;
import pl.dundersztyc.friends.events.FriendshipRemovedEvent;
import pl.dundersztyc.invitations.dto.InvitationRequest;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FriendshipFacadeTest {

    private EventPublisher eventPublisher;
    private FriendshipFacade friendshipFacade;

    @BeforeEach
    void setUp() {
        eventPublisher = mock(EventPublisher.class);
        friendshipFacade = new FriendsConfiguration().friendshipFacade(
                new InMemoryFriendshipRepository(), eventPublisher);
    }


    @Test
    void shouldAddFriendship() {
        var friendshipDto = addFriendship("from", "to");

        assertThat(friendshipDto.idFrom()).isEqualTo("from");
        assertThat(friendshipDto.idTo()).isEqualTo("to");
    }

    @Test
    void shouldThrowWhenAddFriendshipThatAlreadyExist() {
        addFriendship("from", "to");

        assertThrows(FriendshipExistException.class,
                () -> addFriendship("from", "to"));
    }

    @Test
    void shouldThrowWhenAddFriendshipAndRequestIdsAreNull() {
        assertThrows(NullPointerException.class,
                () -> addFriendship(null, null));
    }

    @Test
    void shouldThrowWhenAddFriendshipAndRequestIdsAreEqual() {
        assertThrows(IllegalArgumentException.class,
                () -> addFriendship("from", "from"));
    }

    @Test
    void shouldDeleteFriendship() {
        addFriendship("from", "to");

        friendshipFacade.deleteFriendship(new FriendshipRequest("from", "to"));

        verify(eventPublisher).raise(any(FriendshipRemovedEvent.class));
        // we can create same friendship when previous one is deleted
        addFriendship("from", "to");
    }


    private FriendshipDto addFriendship(String idFrom, String idTo) {
        var request = new FriendshipRequest(idFrom, idTo);
        return friendshipFacade.addFriendship(request);
    }
}