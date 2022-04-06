package dev.toma.gunsrpg.util.properties;

public final class PropertyKey<V> {

    private final V defaultValue;

    private PropertyKey(V value) {
        this.defaultValue = value;
    }

    public static <V> PropertyKey<V> newKey(V value) {
        return new PropertyKey<>(value);
    }

    public static <V> PropertyKey<V> newKey() {
        return newKey(null);
    }

    public V getDefaultValue() {
        return defaultValue;
    }
}
