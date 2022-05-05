package dev.toma.gunsrpg.util.properties;

@FunctionalInterface
public interface IPropertyWriter {

    <V> void setProperty(PropertyKey<V> key, V value);
}
