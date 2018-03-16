package shopping.cart.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import shopping.cart.type.CustomerId;

@Value.Immutable
@JsonDeserialize(builder = CartCreatedBuilder.class)
@JsonTypeName("CartCreated")
public interface CartCreated extends CartEvent {

    CustomerId getCustomerId();

    static CartCreated of(CustomerId customerId) {
        return CartCreatedBuilder.empty()
                .customerId(customerId)
                .build();
    }
}
