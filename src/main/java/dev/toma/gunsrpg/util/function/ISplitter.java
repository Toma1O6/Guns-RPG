package dev.toma.gunsrpg.util.function;

import java.util.List;
import java.util.Map;

public interface ISplitter<K, V> {

    Map<K, List<V>> split(Iterable<V> iterable);
}
