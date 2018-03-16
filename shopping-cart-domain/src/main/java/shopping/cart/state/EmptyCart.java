package shopping.cart.state;

import event.sourcing.command.CommandOutcome;
import shopping.cart.command.AddProduct;
import shopping.cart.event.ProductAdded;

public class EmptyCart extends ShoppingCart {

    public CommandOutcome add(AddProduct cmd) {
        return CommandOutcome.ok(ProductAdded.create(cmd.getProduct(), cmd.getQuantity()));
    }

    CommandOutcome clear() {
        return CommandOutcome.noOp();
    }

    public ShoppingCart onProductAdded(ProductAdded evt) {
        return new NonEmptyCart(evt.getProductId(), evt.getQuantity());
    }
}
