package shopping.cart.state;

import event.sourcing.command.CommandOutcome;
import shopping.cart.command.AddProduct;
import shopping.cart.event.ProductAdded;
import shopping.cart.type.ProductId;
import shopping.cart.type.Quantity;

import java.util.HashMap;
import java.util.Map;

public class NonEmptyCart extends ShoppingCart {

    private Map<ProductId, Quantity> content = new HashMap<>();

    NonEmptyCart(ProductId initialProduct, Quantity initialQuantity) {
        this.content.put(initialProduct, initialQuantity);
    }

    public CommandOutcome add(AddProduct cmd) {
        return CommandOutcome.ok(ProductAdded.create(cmd.getProduct(), cmd.getQuantity()));
    }

    public NonEmptyCart onProductAdded(ProductAdded evt) {
        content.put(evt.getProductId(), evt.getQuantity());
        return this;
    }

    void clear() {

    }


//    void remove(Re)
}
