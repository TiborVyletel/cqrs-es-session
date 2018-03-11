package event.sourcing.identity;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Sequence {

    protected abstract int value();

    public final int toInt() {
        return value();
    }

    public static Sequence of(int seq) {
        return SequenceBuilder.empty()
                .value(seq)
                .build();
    }
}
