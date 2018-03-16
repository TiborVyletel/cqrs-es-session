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
import shopping.cart.command.AddProduct;
import shopping.cart.command.CreateCart;
import shopping.cart.event.CartCreated;
import shopping.cart.state.NonExistingCart;
import shopping.cart.state.ShoppingCart;
import shopping.cart.type.CartId;
import shopping.cart.type.CustomerId;
import shopping.cart.type.ProductId;
import shopping.cart.type.Quantity;
import shopping.cart.wiring.ShoppingCartCommandHandler;
import shopping.cart.wiring.ShoppingCartEventApplicator;

import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ShoppingCartTest {

    static ActorSystem system;

    static ActorRef repository;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("es-system", ConfigFactory.load("jdbc-journal-test.conf"));

        Supplier<ShoppingCart> initial = () -> new NonExistingCart();
        ShoppingCartCommandHandler handler = new ShoppingCartCommandHandler();
        ShoppingCartEventApplicator applicator = new ShoppingCartEventApplicator();

        repository = system.actorOf(RepositoryActor.props(initial, handler, applicator), "ShoppingCarts");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.apply("10 s"), true);
        system = null;
    }


    @Test
    public void createNewCart() {

        TestKit probe = new TestKit(system);

        repository.tell(CreateCart.of(CartId.random(), CustomerId.random()), probe.testActor());

        CommandOutcome outcome = probe.expectMsgClass(CommandOutcome.class);
        assertThat(outcome.events().get(0), is(instanceOf(CartCreated.class)));
    }

    @Test
    public void addProduct_ToExistingCart() {
        TestKit probe = new TestKit(system);
        CartId cartId = CartId.of("urn:corp:shopping-cart:2569644950335");

        AddProduct cmd = AddProduct.create(cartId, ProductId.random(), Quantity.pieces(10));
        repository.tell(cmd, probe.testActor());

        probe.expectMsgClass(CommandOutcome.class);
    }

}
