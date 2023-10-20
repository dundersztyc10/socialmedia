package pl.dundersztyc.invitations.infrastructure.events;

import pl.dundersztyc.common.events.DomainEvent;

import java.time.Instant;

public record InvitationAcceptedEvent(
        Instant when,
        String senderId,
        String receiverId
) implements DomainEvent {
}
