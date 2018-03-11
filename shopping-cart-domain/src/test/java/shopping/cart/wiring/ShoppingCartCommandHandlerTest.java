package shopping.cart.wiring;

import event.sourcing.command.CommandOutcome;
import org.junit.Test;
import shopping.cart.command.CreateCart;
import shopping.cart.state.NonExistingCart;
import shopping.cart.type.CartId;
import shopping.cart.type.CustomerId;

import static com.google.common.truth.Truth.assertThat;

public class ShoppingCartCommandHandlerTest {

    private ShoppingCartCommandHandler handler = new ShoppingCartCommandHandler();

    @Test
    public void doCreate() {
        CustomerId customerId = CustomerId.random();

        CommandOutcome outcome = handler.execute(new NonExistingCart(), CreateCart.of(CartId.random(), customerId));

        assertThat(outcome.status()).isEqualTo(CommandOutcome.StatusCode.OK);
        assertThat(outcome.events()).hasSize(1);
    }

}