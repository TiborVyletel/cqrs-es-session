package cqrs.es.session.application;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import com.typesafe.config.ConfigFactory;
import event.sourcing.command.CommandOutcome;
import event.sourcing.runtime.akka.RepositoryActor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.Duration;
import shopping.cart.command.CreateCart;
import shopping.cart.state.NonExistingCart;
import shopping.cart.state.ShoppingCart;
import shopping.cart.type.CartId;
import shopping.cart.type.CustomerId;
import shopping.cart.wiring.ShoppingCartCommandHandler;
import shopping.cart.wiring.ShoppingCartEventApplicator;

import java.util.function.Supplier;

public class ShoppingCartTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("es-system", ConfigFactory.load("jdbc-journal-test.conf"));
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.apply("10 s"), true);
        system = null;
    }


    @Test
    public void initializeActor() {
        Supplier<ShoppingCart> initial = () -> new NonExistingCart();
        ShoppingCartCommandHandler handler = new ShoppingCartCommandHandler();
        ShoppingCartEventApplicator applicator = new ShoppingCartEventApplicator();

        ActorRef repo = system.actorOf(RepositoryActor.props(initial, handler, applicator), "ShoppingCarts");

        TestKit probe = new TestKit(system);

        repo.tell(CreateCart.of(CartId.random(), CustomerId.random()), probe.testActor());

        probe.expectMsgClass(CommandOutcome.class);
    }

}
