package shopping.cart.command;

import org.immutables.value.Value;
import shopping.cart.type.CartId;
import shopping.cart.type.CustomerId;

@Value.Immutable
public abstract class CreateCart extends CartCommand {

    public abstract CustomerId getCustomerId();


    public static CreateCart of(CartId target, CustomerId customerId) {
        return CreateCartBuilder.empty()
                .target(target)
                .customerId(customerId)
                .build();
    }
}
