package shopping.cart.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(converter = QuantityToString.class)
@JsonDeserialize(converter = StringToQuantity.class)
public interface Quantity {

    String PIECES = "pcs";
    String METRES = "m";

    double getAmount();

    String getUnit();

    static Quantity of(double amount, String unit) {
        return QuantityBuilder.empty()
                .amount(amount)
                .unit(unit)
                .build();
    }

    static Quantity pieces(int amount) {
        return QuantityBuilder.empty()
                .amount(amount)
                .unit(PIECES)
                .build();
    }

    static Quantity metres(double length) {
        return QuantityBuilder.empty()
                .amount(length)
                .unit(METRES)
                .build();
    }

}

class QuantityToString implements Converter<Quantity, String> {

    @Override
    public String convert(Quantity value) {
        return value.getAmount() + " " + value.getUnit();
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructSimpleType(Quantity.class, new JavaType[0]);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructSimpleType(String.class, new JavaType[0]);
    }
}

class StringToQuantity implements Converter<String, Quantity> {

    @Override
    public Quantity convert(String value) {
        String[] split = value.split(" ");
        return Quantity.of(Double.parseDouble(split[0]), split[1]);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructSimpleType(String.class, new JavaType[0]);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructSimpleType(Quantity.class, new JavaType[0]);
    }
}