package shopping.cart.event;


import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import shopping.cart.type.ProductId;
import shopping.cart.type.Quantity;

@Value.Immutable
@JsonDeserialize(builder = ProductAddedBuilder.class)
@JsonTypeName("ProductAdded")
public interface ProductAdded extends CartEvent {

    ProductId getProductId();

    Quantity getQuantity();

    static ProductAdded create(ProductId productId, Quantity quantity) {
        return ProductAddedBuilder.empty()
                .productId(productId)
                .quantity(quantity)
                .build();
    }

}
