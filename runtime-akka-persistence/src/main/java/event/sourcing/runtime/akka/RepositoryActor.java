package event.sourcing.runtime.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import event.sourcing.command.Command;
import event.sourcing.command.CommandHandler;
import event.sourcing.event.DomainEvent;
import event.sourcing.event.EventApplicator;
import event.sourcing.identity.AggregateId;

import java.util.function.Supplier;

public class RepositoryActor<A, C extends Command> extends AbstractActor {

    private final Supplier<A> initialStateFactory;
    private final CommandHandler<A, C> cmdHandler;
    private final EventApplicator<A, A, DomainEvent> applicator;

    private final LoggingAdapter log = Logging.getLogger(this);

    protected RepositoryActor(Supplier<A> initialStateFactory, CommandHandler<A, C> cmdHandler, EventApplicator<A, A, DomainEvent> applicator) {
        this.initialStateFactory = initialStateFactory;
        this.cmdHandler = cmdHandler;
        this.applicator = applicator;
    }

    public static <A, C extends Command> Props props(Supplier<A> initialStateFactory, CommandHandler<A, C> cmdHandler, EventApplicator<A, A, ? extends DomainEvent> applicator) {
        return Props.create(RepositoryActor.class, () -> new RepositoryActor(initialStateFactory, cmdHandler, applicator));
    }

    public void preStart() {
        log.info("Repository Starting: {}", getSelf().path());
    }


    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Command.class, this::handleCommand)
                .build();
    }

    void handleCommand(Command<AggregateId> cmd) {
        ActorRef child = getContext().findChild(cmd.getTarget().toString())
                .orElseGet(() -> createNewChild(cmd.getTarget()));
        child.tell(cmd, sender());
    }

    ActorRef createNewChild(AggregateId id) {
        log.info("About to create actor for {}", id);
        return getContext().actorOf(createPropsForNewAggregate(id), id.toString());
    }

    Props createPropsForNewAggregate(AggregateId id) {
        return EventSourcingActor.props(id, this.initialStateFactory, cmdHandler, applicator);
    }
}
