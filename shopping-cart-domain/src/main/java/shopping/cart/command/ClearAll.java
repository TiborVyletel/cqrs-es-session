package shopping.cart.command;

import org.immutables.value.Value;
import shopping.cart.type.CartId;


@Value.Immutable
public abstract class ClearAll extends CartCommand {

    public static ClearAll create(CartId cartId) {
        return ClearAllBuilder.empty()
                .target(cartId)
                .build();
    }
}
