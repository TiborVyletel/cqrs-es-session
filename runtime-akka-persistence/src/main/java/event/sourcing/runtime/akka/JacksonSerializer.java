package event.sourcing.runtime.akka;

import akka.persistence.PersistentImpl;
import akka.persistence.PersistentRepr;
import akka.serialization.JSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import event.sourcing.event.DomainEvent;
import event.sourcing.identity.AggregateId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shopping.cart.event.CartCreated;
import shopping.cart.event.ProductAdded;
import shopping.cart.type.CustomerId;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JacksonSerializer extends JSerializer {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonSerializer.class);

    ObjectMapper mapper = new ObjectMapper();

    {
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, JsonTypeInfo.As.PROPERTY);
        mapper.registerModule(new IdentifiersModule());
        mapper.addMixIn(DomainEvent.class, DomainEventMixin.class);
        mapper.addMixIn(PersistentRepr.class, PersistentReprMixin.class);
    }

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
        try {
            return mapper.readValue(bytes, TypeFactory.defaultInstance().constructSimpleType(DomainEvent.class, new JavaType[0]));
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


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CartCreated.class),
        @JsonSubTypes.Type(value = ProductAdded.class)
})
class DomainEventMixin {

}

@JsonDeserialize(converter = MapToRepr.class)
abstract class PersistentReprMixin {

    @JsonProperty("payload")
    abstract DomainEvent payload();

    @JsonProperty("manifest")
    abstract String manifest();

    @JsonProperty("persistenceId")
    abstract String persistenceId();

    @JsonProperty("seq")
    abstract long sequenceNr();

}

class MapToRepr implements Converter<Map<String, Object>, PersistentRepr> {

    private JavaType[] empty = new JavaType[0];

    @Override
    public PersistentRepr convert(Map<String, Object> map) {
        return PersistentImpl.apply(map.get("payload"), (Integer) map.get("seq"), (String) map.get("persistenceId"), "", false, null, "");
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructMapType(HashMap.class, typeFactory.constructSimpleType(String.class, empty),
                typeFactory.constructSimpleType(Object.class, empty));
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructSimpleType(PersistentRepr.class, empty);
    }
}