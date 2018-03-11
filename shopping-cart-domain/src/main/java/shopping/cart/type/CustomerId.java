package shopping.cart.type;

import event.sourcing.identity.UrnBasedAggregateId;
import org.hashids.Hashids;

import java.time.Instant;
import java.util.Random;

public class CustomerId extends UrnBasedAggregateId {

    private final String id;

    private CustomerId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    public static CustomerId random() {
        return new CustomerId("urn:corp:customer:" + CustomerIdInternals.random());
    }

    public static CustomerId of(String str) {
        return new CustomerId(str);
    }
}

class CustomerIdInternals {

    private static Hashids hashids = new Hashids("Immutability Changes Everything");
    private static Random RND = new Random(System.nanoTime());
    private static Instant START = Instant.parse("2018-01-01T00:00:00Z");

    public static String random() {
        return hashids.encode(Thread.currentThread().getId(), rndVal(), START.minusMillis(System.currentTimeMillis()).toEpochMilli() / 1000);
    }

    public static long rndVal() {
        return RND.nextInt(32678);
    }
}