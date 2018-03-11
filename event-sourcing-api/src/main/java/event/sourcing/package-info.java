@Value.Style(
        builderVisibility = Value.Style.BuilderVisibility.PACKAGE,
        visibility = Value.Style.ImplementationVisibility.PRIVATE,
        depluralize = true,
        typeImmutable = "Imm*",
        newBuilder = "empty",
        jdkOnly = true
)
package event.sourcing;

import org.immutables.value.Value;