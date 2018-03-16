package shopping.cart.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import event.sourcing.identity.AggregateId;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(converter = StringToProductId.class)
public abstract class ProductId implements AggregateId {

    protected abstract String value();

    @Override
    public String toString() {
        return value();
    }

    public static ProductId of(String value) {
        return ProductIdBuilder.empty()
                .value(value)
                .build();
    }

    public static ProductId random() {
        return ProductIdBuilder.empty()
                .value("urn:corp:product:" + System.nanoTime())
                .build();
    }
}

class StringToProductId implements Converter<String, ProductId> {

    @Override
    public ProductId convert(String value) {
        return ProductId.of(value);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructSimpleType(String.class, new JavaType[0]);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructSimpleType(ProductId.class, new JavaType[0]);
    }
}