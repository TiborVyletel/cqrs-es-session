package event.sourcing.event;

/**
 * Event application is a function f(state, event) => state;
 * An existing state of aggregate is changed by a domain event to a next state
 *
 * @param <A1>
 * @param <A2>
 * @param <E>
 */
@FunctionalInterface
public interface EventApplicator<A1, A2, E extends DomainEvent> {

    A2 apply(A1 aggregate, E event);
}
