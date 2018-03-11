package shopping.cart.type;

import event.sourcing.identity.AggregateId;
import org.immutables.value.Value;

@Value.Immutable
public abstract class CartId implements AggregateId {

    protected abstract String value();

    @Override
    public String toString() {
        return value();
    }

    public static CartId of(String value) {
        throw new UnsupportedOperationException("!");
    }

    public static CartId random() {
        return CartIdBuilder.empty()
                .value("urn:corp:shopping-cart:" + System.nanoTime())
                .build();
    }
}
