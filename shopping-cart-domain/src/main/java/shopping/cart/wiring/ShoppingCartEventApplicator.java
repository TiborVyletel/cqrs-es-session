package shopping.cart.wiring;

import event.sourcing.event.EventApplicator;
import org.apache.commons.lang3.tuple.Pair;
import shopping.cart.event.CartCreated;
import shopping.cart.event.CartEvent;
import shopping.cart.event.ProductAdded;
import shopping.cart.state.EmptyCart;
import shopping.cart.state.NonEmptyCart;
import shopping.cart.state.NonExistingCart;
import shopping.cart.state.ShoppingCart;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ShoppingCartEventApplicator implements EventApplicator<ShoppingCart, ShoppingCart, CartEvent> {

    private Map<Pair<Class, Class>, BiFunction> appliers = new HashMap<>();

    {
        register(NonExistingCart.class, CartCreated.class, NonExistingCart::onCreated);

        // Empty cart
        register(EmptyCart.class, ProductAdded.class, EmptyCart::onProductAdded);

        // Non Empty
        register(NonEmptyCart.class, ProductAdded.class, NonEmptyCart::onProductAdded);
    }

    private <A, E, X> void register(Class<A> clasA, Class<E> classE, BiFunction<A, E, X> applier) {
        appliers.put(Pair.of(clasA, classE), applier);
    }


    @Override
    public ShoppingCart apply(ShoppingCart aggregate, CartEvent event) {
        return (ShoppingCart) appliers.get(Pair.of(aggregate.getClass(), event.getClass().getInterfaces()[0])).apply(aggregate, event);
    }
}
