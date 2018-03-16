package event.sourcing.command;

/**
 * A function for computing an effect of given command on the Aggregate instance.
 * Must be side effect free - aggregate must not be (observably) changed
 */
@FunctionalInterface
public interface CommandHandler<A, C extends Command> {

    CommandOutcome execute(A aggregate, C command);

}
