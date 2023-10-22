package pl.dundersztyc.friends.events;

import pl.dundersztyc.common.events.DomainEvent;

public record FriendshipRemovedEvent(String idFrom, String idTo)
        implements DomainEvent {
}
