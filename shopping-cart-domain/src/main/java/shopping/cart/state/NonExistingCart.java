package shopping.cart.state;

import event.sourcing.command.CommandOutcome;
import shopping.cart.command.CreateCart;
import shopping.cart.event.CartCreated;

/**
 * This is the initial state fora shopping cart - it can only
 */
public class NonExistingCart extends ShoppingCart {

    public CommandOutcome create(CreateCart cmd) {
        return CommandOutcome.ok(CartCreated.of(cmd.getCustomerId()));
    }

    public EmptyCart onCreated(CartCreated evt) {
        return new EmptyCart();
    }
}
