package dev.toma.gunsrpg.util.math;

import com.google.common.collect.Iterators;
import dev.toma.gunsrpg.util.object.LazyLoader;

import java.util.*;
import java.util.function.ToIntFunction;

public class WeightedRandom<T> implements Iterable<T> {

    protected static Random random = new Random();
    protected final List<T> values;
    protected final ToIntFunction<T> toIntFunction;
    private final LazyLoader<Integer> totalValue;

    public WeightedRandom(ToIntFunction<T> toIntFunction, T[] values) {
        this(toIntFunction, Arrays.asList(values));
    }

    public WeightedRandom(ToIntFunction<T> toIntFunction, List<T> values) {
        this.toIntFunction = toIntFunction;
        this.values = values;
        this.totalValue = new LazyLoader<>(this::gatherAll);
    }

    public T getRandom() {
        int total = totalValue.get();
        int weight = random.nextInt(total);
        for (int idx = values.size() - 1; idx >= 0; idx--) {
            T t = values.get(idx);
            weight -= toIntFunction.applyAsInt(t);
            if (weight < 0) {
                return t;
            }
        }
        return null;
    }

    public List<T> getValues() {
        return values;
    }

    public int getValueCount() {
        return values.size();
    }

    @Override
    public Iterator<T> iterator() {
        return this.values.iterator();
    }

    private int gatherAll() {
        int i = 0;
        for (T t : values)
            i += toIntFunction.applyAsInt(t);
        return i;
    }
}
