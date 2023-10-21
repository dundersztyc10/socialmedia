package pl.dundersztyc.invitations.events;

import pl.dundersztyc.common.events.DomainEvent;

import java.time.Instant;

public record InvitationAcceptedEvent(
        Instant when,
        String senderId,
        String receiverId
) implements DomainEvent {
}
