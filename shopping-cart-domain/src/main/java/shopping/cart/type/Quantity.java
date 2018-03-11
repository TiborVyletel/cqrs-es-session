package shopping.cart.type;

import org.immutables.value.Value;

@Value.Immutable
public interface Quantity {

    double getAmount();

    String getUnit();

    static Quantity pieces(int amount) {
        return null;
    }
}
