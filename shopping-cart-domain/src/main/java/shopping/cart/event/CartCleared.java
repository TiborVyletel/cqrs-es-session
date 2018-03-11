package shopping.cart.event;

import org.immutables.value.Value;

@Value.Immutable
public interface CartCleared extends CartEvent {

    static CartCleared create() {
        return CartClearedBuilder.empty().build();
    }
}
