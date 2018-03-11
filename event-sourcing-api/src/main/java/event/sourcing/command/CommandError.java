package event.sourcing.command;

import org.immutables.value.Value;

import java.util.Map;
import java.util.Optional;

@Value.Immutable
public interface CommandError {

    String getCode();

    String getMessage();

    Map<String, Object> getContext();

    Optional<Throwable> getException();

    static CommandError unsupportedCommand(Command<?> cmd, Object state) {
        return CommandErrorBuilder.empty()
                .code("UNSUPPORTED_COMMAND")
                .message("Cannot perform " + cmd.getClass().getSimpleName() + " on " + state.getClass())
                .putContext("CMD", cmd)
                .putContext("STATE_CLASS", state.getClass()) // state can be mutable, do not leak !
                .build();

    }
}
