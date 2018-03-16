@Value.Style(
        builderVisibility = Value.Style.BuilderVisibility.PACKAGE,
        visibility = Value.Style.ImplementationVisibility.PACKAGE,
        implementationNestedInBuilder = true,
        depluralize = true,
        typeImmutable = "Imm*",
        newBuilder = "empty",
        jdkOnly = true,
        passAnnotations = {JsonTypeName.class}

)
package shopping.cart;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.immutables.value.Value;