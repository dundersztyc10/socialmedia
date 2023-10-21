package pl.dundersztyc.friends.listeners;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.dundersztyc.friends.FriendFacade;
import pl.dundersztyc.invitations.events.InvitationAcceptedEvent;

@Component
@RequiredArgsConstructor
public class InvitationAcceptedListener {

    @EventListener
    public void on(InvitationAcceptedEvent event) {
        Logger LOGGER = LoggerFactory.getLogger(InvitationAcceptedEvent.class);
        LOGGER.info("INVITATION ACCEPTED EVENT - LISTENER");
    }
}
