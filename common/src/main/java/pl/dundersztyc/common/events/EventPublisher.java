package pl.dundersztyc.common.events;

public interface EventPublisher {
    void raise(DomainEvent event);
}
