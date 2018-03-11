package shopping.cart.command;

import shopping.cart.type.ProductId;

public abstract class AddProduct extends CartCommand {

    public abstract ProductId getProduct();

    public abstract int getCount();
}
