package shopping.cart.wiring;

import event.sourcing.command.CommandHandler;
import event.sourcing.command.CommandOutcome;
import shopping.cart.command.AddProduct;
import shopping.cart.command.CartCommand;
import shopping.cart.command.CreateCart;
import shopping.cart.state.EmptyCart;
import shopping.cart.state.NonEmptyCart;
import shopping.cart.state.NonExistingCart;
import shopping.cart.state.ShoppingCart;

public class ShoppingCartCommandHandler implements CommandHandler<ShoppingCart, CartCommand> {

    @Override
    public CommandOutcome execute(ShoppingCart aggregate, CartCommand command) {
        if (aggregate instanceof NonExistingCart) {
            return doHandleInitial((NonExistingCart) aggregate, command);
        }
        if (aggregate instanceof EmptyCart) {
            return doHandleEmpty((EmptyCart) aggregate, command);
        }
        if (aggregate instanceof NonEmptyCart) {
            return doHandleNonEmpty((NonEmptyCart) aggregate, command);
        }
        return CommandOutcome.badCommand(command, aggregate);
    }

    private CommandOutcome doHandleInitial(NonExistingCart aggregate, CartCommand command) {
        if (command instanceof CreateCart) {
            return aggregate.create((CreateCart) command);
        }
        return CommandOutcome.badCommand(command, aggregate);
    }

    private CommandOutcome doHandleEmpty(EmptyCart aggregate, CartCommand command) {
        if (command instanceof AddProduct) {
            return aggregate.add((AddProduct) command);
        }
        return CommandOutcome.badCommand(command, aggregate);
    }

    private CommandOutcome doHandleNonEmpty(NonEmptyCart aggregate, CartCommand command) {
        if (command instanceof AddProduct) {
            return aggregate.add((AddProduct) command);
        }
        return CommandOutcome.badCommand(command, aggregate);
    }
}
