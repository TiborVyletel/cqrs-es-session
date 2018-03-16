package shopping.cart.command;

import org.immutables.value.Value;
import shopping.cart.type.CartId;
import shopping.cart.type.ProductId;
import shopping.cart.type.Quantity;

@Value.Immutable
public abstract class AddProduct extends CartCommand {

    public abstract ProductId getProduct();

    public abstract Quantity getQuantity();

    public static AddProduct create(CartId cardId, ProductId productId, Quantity quantity) {
        return AddProductBuilder.empty()
                .target(cardId)
                .product(productId)
                .quantity(quantity)
                .build();
    }

}
