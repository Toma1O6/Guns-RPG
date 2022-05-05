package dev.toma.gunsrpg.util.properties;

import java.util.HashMap;
import java.util.Map;

public final class PropertyRegistry {

    private static final Map<String, PropertyKey<?>> REGISTRY = new HashMap<>();

    public static void registerKey(PropertyKey<?> key) {
        REGISTRY.put(key.getId(), key);
    }

    @SuppressWarnings("unchecked")
    public static <V> PropertyKey<V> getById(String id) {
        return (PropertyKey<V>) REGISTRY.get(id);
    }

    private PropertyRegistry() {}
}
