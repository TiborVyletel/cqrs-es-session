package event.sourcing.command;

import event.sourcing.identity.AggregateId;

import java.util.UUID;

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
