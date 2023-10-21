package pl.dundersztyc.invitations.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.dundersztyc.friends.events.FriendshipRemovedEvent;

@Component
class FriendshipRemovedListener {

    @EventListener
    public void on(FriendshipRemovedEvent event) {
        Logger LOGGER = LoggerFactory.getLogger(FriendshipRemovedListener.class);
        LOGGER.info("FRIENDSHIP REMOVED EVENT - LISTENER");
    }
}
