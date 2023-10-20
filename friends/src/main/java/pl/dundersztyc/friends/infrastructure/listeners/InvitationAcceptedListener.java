package pl.dundersztyc.friends.infrastructure.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import pl.dundersztyc.friends.FriendFacade;
import pl.dundersztyc.invitations.infrastructure.events.InvitationAcceptedEvent;

@RequiredArgsConstructor
class InvitationAcceptedListener {

    private final FriendFacade friendFacade;

    @EventListener
    public void on(InvitationAcceptedEvent event) {
    }
}
