package event.sourcing.command;

@FunctionalInterface
public interface CommandHandler<A, C extends Command> {

    CommandOutcome execute(A aggregate, C command);

}
