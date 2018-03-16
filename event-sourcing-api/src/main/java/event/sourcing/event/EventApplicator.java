package event.sourcing.event;

/**
 * Event application is a function f(state, event) => state;
 * An existing state of aggregate is changed by a domain event to a next state
 * Event is a fact which already happened, thus its application to aggregate must not fail.
 */
@FunctionalInterface
public interface EventApplicator<A1, A2, E extends DomainEvent> {

    A2 apply(A1 aggregate, E event);
}
