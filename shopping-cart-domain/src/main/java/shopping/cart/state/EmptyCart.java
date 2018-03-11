package shopping.cart.state;

import shopping.cart.command.AddProduct;

public class EmptyCart extends ShoppingCart {

    void add(AddProduct cmd) {

    }

    void clear() {
        // do nothing
    }

//    void remove(Re)
}
