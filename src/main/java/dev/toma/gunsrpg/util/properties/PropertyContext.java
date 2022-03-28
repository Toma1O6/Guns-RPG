package dev.toma.gunsrpg.util.properties;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
}
