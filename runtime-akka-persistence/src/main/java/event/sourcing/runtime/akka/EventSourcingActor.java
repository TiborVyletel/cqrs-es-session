package event.sourcing.runtime.akka;

import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.persistence.AbstractPersistentActor;
import akka.persistence.PersistentRepr;
import akka.persistence.RecoveryCompleted;
import event.sourcing.command.Command;
import event.sourcing.command.CommandHandler;
import event.sourcing.command.CommandOutcome;
import event.sourcing.event.DomainEvent;
import event.sourcing.event.EventApplicator;
import event.sourcing.identity.AggregateId;

import java.util.function.Supplier;

public class EventSourcingActor<ID extends AggregateId, A> extends AbstractPersistentActor {

    private final ID id;
    private A state;
    private final CommandHandler<A, Command> handler;
    private final EventApplicator<A, A, DomainEvent> applicator;

    private final LoggingAdapter log = Logging.getLogger(this);

    protected EventSourcingActor(ID id, Supplier<A> factory, CommandHandler<A, ?> handler, EventApplicator<A, A, ? extends DomainEvent> applicator) {
        this.id = id;
        this.state = factory.get();
        this.handler = (CommandHandler<A, Command>) handler;
        this.applicator = (EventApplicator<A, A, DomainEvent>) applicator;
    }

    public static <ID extends AggregateId, A> Props props(ID id, Supplier<A> factory, CommandHandler<A, ?> handler, EventApplicator<A, A, ?> applicator) {
        return Props.create(EventSourcingActor.class, () -> new EventSourcingActor(id, factory, handler, applicator));
    }

    public void preStart() {
        log.info("Starting persistent actor: {}", getSelf().path());
    }


    @Override
    public String persistenceId() {
        return id.toString();
    }

    @Override
    public Receive createReceiveRecover() {
        return receiveBuilder()
                .match(PersistentRepr.class, m -> {
                    log.info(m.toString());
                })
                .match(DomainEvent.class, this::applyEvent)
                .match(RecoveryCompleted.class, evt -> {
                    log.info("Recovery completed. Current sequence: {}", lastSequenceNr());
                })
                .build();
    }

    void applyEvent(DomainEvent evt) {
        this.state = applicator.apply(state, evt);
        log.info("Event applied during recovery: {}", evt);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Command.class, c -> {
                    CommandOutcome outcome = handler.execute(state, c);
                    persistAll(outcome.events(), (e) -> {
                        log.info("Persisted!");
                        applyEvent(e);
                        sender().tell(outcome, self());
                    });
                })
                .matchEquals("print", s -> {
                    System.out.println("Hello");
                    sender().tell("printed", self());
                })
                .build();
    }
}