package dev.toma.gunsrpg.util.properties;

import net.minecraft.network.PacketBuffer;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public final class PropertyContext {

    private final Map<PropertyKey<?>, Object> map;

    private PropertyContext() {
        map = new IdentityHashMap<>();
    }

    public static PropertyContext create() {
        return new PropertyContext();
    }

    public <V> void setProperty(PropertyKey<V> key, V value) {
        map.put(key, value);
    }

    public <V> V getProperty(PropertyKey<V> key) {
        V value = (V) map.get(key);
        return value != null ? value : key.getDefaultValue();
    }

    public <V> Optional<V> getOptionally(PropertyKey<V> key) {
        return Optional.ofNullable(this.getProperty(key));
    }

    public <V> void handleConditionally(PropertyKey<V> key, Predicate<V> condition, Consumer<V> action) {
        this.getOptionally(key).ifPresent(value -> {
            if (condition.test(value)) {
                action.accept(value);
            }
        });
    }

    public <V> void encode(PacketBuffer buffer) {
        List<Map.Entry<PropertyKey<?>, Object>> synchronizables = map.entrySet().stream().filter(entry -> entry.getKey().isSerializable()).collect(Collectors.toList());
        buffer.writeVarInt(synchronizables.size());
        for (Map.Entry<PropertyKey<?>, Object> entry : synchronizables) {
            PropertyKey<V> key = (PropertyKey<V>) entry.getKey();
            V value = (V) entry.getValue();
            buffer.writeUtf(key.getId());
            IPropertySerializer<V> serializer = key.getSerializer();
            serializer.encode(buffer, value);
        }
    }

    public <V> void decode(PacketBuffer buffer) {
        int properties = buffer.readVarInt();
        for (int i = 0; i < properties; i++) {
            String key = buffer.readUtf();
            PropertyKey<V> propertyKey = PropertyRegistry.getById(key);
            IPropertySerializer<V> serializer = propertyKey.getSerializer();
            V value = serializer.decode(buffer);
            setProperty(propertyKey, value);
        }
    }
}
