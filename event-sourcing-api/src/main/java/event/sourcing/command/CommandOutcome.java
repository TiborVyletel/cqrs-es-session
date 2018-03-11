package event.sourcing.command;

import event.sourcing.event.DomainEvent;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
public interface CommandOutcome {

    enum StatusCode {
        OK,
        ACCEPTED,
        BAD_COMMAND;
    }

    StatusCode status();

    Optional<CommandError> error();

    List<DomainEvent> events();

    static CommandOutcome ok(DomainEvent... events) {
        return CommandOutcomeBuilder.empty()
                .status(StatusCode.OK)
                .addEvent(events)
                .build();
    }

    static CommandOutcome badCommand(Command<?> command, Object state) {
        return CommandOutcomeBuilder.empty()
                .status(StatusCode.BAD_COMMAND)
                .error(CommandError.unsupportedCommand(command, state))
                .build();
    }

}
