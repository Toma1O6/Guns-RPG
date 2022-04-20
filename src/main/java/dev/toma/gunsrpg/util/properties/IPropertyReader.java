package dev.toma.gunsrpg.util.properties;

import java.util.Optional;

public interface IPropertyReader {

    <V> V getProperty(PropertyKey<V> key);

    <V>Optional<V> getOptionally(PropertyKey<V> key);
}
