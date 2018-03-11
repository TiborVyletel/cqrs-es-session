package shopping.cart.command;

import event.sourcing.command.Command;
import shopping.cart.type.CartId;

public abstract class CartCommand extends Command<CartId> {

}
