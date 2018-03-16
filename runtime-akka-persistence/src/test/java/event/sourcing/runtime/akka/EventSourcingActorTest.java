package event.sourcing.runtime.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import com.typesafe.config.ConfigFactory;
import event.sourcing.identity.AggregateId;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import scala.concurrent.duration.Duration;

@Ignore
public class EventSourcingActorTest {

    static ActorSystem system;

    @BeforeClass
    public static void setUp() {
        system = ActorSystem.create("persistence-sys", ConfigFactory.load("jdbc-journal-test.conf"));
    }

    @AfterClass
    public static void tearDown() {
        TestKit.shutdownActorSystem(system, Duration.apply("10 s"), true);
        system = null;
    }


    @Test
    public void initializeActor() {
        ActorRef ref = system.actorOf(EventSourcingActor.props(new TestId(), () -> new State(), null, null));

        TestKit probe = new TestKit(system);
        ref.tell("print", probe.testActor());


        probe.expectMsg("printed");
    }
}

class TestId implements AggregateId {
    @Override
    public String toString() {
        return "hello-1";
    }
}

class State {

}
