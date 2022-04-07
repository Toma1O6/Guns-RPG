package dev.toma.gunsrpg.util.properties;

public final class PropertyKey<V> {

    private final String key;
    private final V defaultValue;
    private final IPropertySerializer<V> serializer;

    private PropertyKey(String key, V value, IPropertySerializer<V> serializer) {
        this.key = key;
        this.defaultValue = value;
        this.serializer = serializer;
        PropertyRegistry.registerKey(this);
    }

    public static <V> PropertyKey<V> newSynchronizedKey(String key, V value, IPropertySerializer<V> serializer) {
        return new PropertyKey<>(key, value, serializer);
    }

    public static <V> PropertyKey<V> newKey(String key, V value) {
        return newSynchronizedKey(key, value, null);
    }

    public static <V> PropertyKey<V> newKey(String key) {
        return newKey(key, null);
    }

    public V getDefaultValue() {
        return defaultValue;
    }

    public IPropertySerializer<V> getSerializer() {
        return serializer;
    }

    public boolean isSerializable() {
        return serializer != null;
    }

    public String getId() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyKey<?> that = (PropertyKey<?>) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return "PropertyKey{" +
                "key='" + key + '\'' +
                ", defaultValue=" + defaultValue +
                '}';
    }
}
