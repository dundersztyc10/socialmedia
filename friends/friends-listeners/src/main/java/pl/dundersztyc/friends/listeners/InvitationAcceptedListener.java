package pl.dundersztyc.friends.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.dundersztyc.friends.FriendshipFacade;
import pl.dundersztyc.friends.dto.FriendshipRequest;
import pl.dundersztyc.invitations.events.InvitationAcceptedEvent;

@Component
@RequiredArgsConstructor
public class InvitationAcceptedListener {

    private final FriendshipFacade friendshipFacade;

    @EventListener
    public void handleInvitationAccepted(InvitationAcceptedEvent event) {
        friendshipFacade.addFriendship(
                new FriendshipRequest(event.senderId(), event.receiverId()));
    }
}
