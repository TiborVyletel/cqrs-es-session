package shopping.cart.event;

import org.immutables.value.Value;
import shopping.cart.type.CustomerId;

@Value.Immutable
public interface CartCreated extends CartEvent {

    CustomerId getCustomerId();

    static CartCreated of(CustomerId customerId) {
        return CartCreatedBuilder.empty()
                .customerId(customerId)
                .build();
    }
}
