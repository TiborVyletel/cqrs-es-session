package shopping.cart.type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import event.sourcing.identity.AggregateId;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(builder = CartIdBuilder.class)
public abstract class CartId implements AggregateId {

    protected abstract String value();

    @Override
    public String toString() {
        return value();
    }

    public static CartId of(String value) {
        return CartIdBuilder.empty()
                .value(value)
                .build();
    }

    public static CartId random() {
        return CartIdBuilder.empty()
                .value("urn:corp:shopping-cart:" + System.nanoTime())
                .build();
    }
}
