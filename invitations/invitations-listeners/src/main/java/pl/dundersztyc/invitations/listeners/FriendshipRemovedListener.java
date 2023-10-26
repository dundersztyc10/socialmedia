package pl.dundersztyc.invitations.listeners;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.dundersztyc.friends.events.FriendshipRemovedEvent;
import pl.dundersztyc.invitations.InvitationFacade;
import pl.dundersztyc.invitations.dto.InvitationRequest;

@Component
@RequiredArgsConstructor
class FriendshipRemovedListener {

    private final InvitationFacade invitationFacade;

    @EventListener
    public void handleFriendshipRemoved(FriendshipRemovedEvent event) {
        Logger LOGGER = LoggerFactory.getLogger(FriendshipRemovedListener.class);
        LOGGER.info("FRIENDSHIP REMOVED EVENT - LISTENER");
        invitationFacade.deleteInvitation(new InvitationRequest(event.idFrom(), event.idTo()));
    }
}
