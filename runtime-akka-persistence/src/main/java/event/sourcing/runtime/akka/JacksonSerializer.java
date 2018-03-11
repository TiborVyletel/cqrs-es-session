package event.sourcing.runtime.akka;

import akka.serialization.JSerializer;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import event.sourcing.event.DomainEvent;
import event.sourcing.identity.AggregateId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shopping.cart.type.CustomerId;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

public class JacksonSerializer extends JSerializer {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonSerializer.class);

    ObjectMapper mapper = new ObjectMapper();

    {
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, JsonTypeInfo.As.PROPERTY);
        mapper.registerModule(new IdentifiersModule());
        mapper.addMixIn(DomainEvent.class, DomainEventMixin.class);
    }

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
        try {
            return mapper.readValue(bytes, TypeFactory.defaultInstance().constructCollectionType(List.class, DomainEvent.class));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int identifier() {
        return 784512369;
    }

    @Override
    public byte[] toBinary(Object o) {
        try {
            String strVal = mapper.writeValueAsString(o);
            LOG.info("Serialized: {}", strVal);
            return strVal.getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean includeManifest() {
        return false;
    }
}

class IdentifiersModule extends SimpleModule {

    public IdentifiersModule() {
        addSerializer(AggregateId.class, new AggregateIdSerializer());
        addDeserializer(CustomerId.class, new AggregateIdDeserializer<>(CustomerId::of));
    }
}

class AggregateIdSerializer extends JsonSerializer<AggregateId> {

    @Override
    public void serialize(AggregateId aggregateId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(aggregateId.toString());
    }
}

class AggregateIdDeserializer<T> extends JsonDeserializer<T> {

    private final Function<String, T> factory;

    public AggregateIdDeserializer(Function<String, T> factory) {

        this.factory = factory;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return factory.apply(jsonParser.getText());
    }
}


@JsonTypeInfo(property = "@class", use = JsonTypeInfo.Id.MINIMAL_CLASS)
class DomainEventMixin {

}