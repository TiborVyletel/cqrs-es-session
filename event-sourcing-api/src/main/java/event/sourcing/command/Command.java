package event.sourcing.command;

import event.sourcing.identity.AggregateId;

import java.util.UUID;

/**
 * A message representing a task to be performed on a specific aggregate. That's why command always contains the
 * identity of aggregate which ought to be changed (target).
 */
public abstract class Command<ID extends AggregateId> {

    private final String id = UUID.randomUUID().toString();

    /**
     * Identity of the command itself
     */
    public final String getId() {
        return this.id;
    }

    public abstract ID getTarget();
}
