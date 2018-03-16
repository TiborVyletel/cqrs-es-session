package shopping.cart.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = CartClearedBuilder.class)
public interface CartCleared extends CartEvent {

    static CartCleared create() {
        return CartClearedBuilder.empty().build();
    }
}
