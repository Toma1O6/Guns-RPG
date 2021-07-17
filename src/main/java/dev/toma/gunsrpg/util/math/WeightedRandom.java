package dev.toma.gunsrpg.util.math;

import dev.toma.gunsrpg.util.object.LazyLoader;

import java.util.Random;
import java.util.function.ToIntFunction;

public class WeightedRandom<T> {

    protected static Random random = new Random();
    protected final T[] values;
    protected final ToIntFunction<T> toIntFunction;
    private final LazyLoader<Integer> totalValue;

    public WeightedRandom(ToIntFunction<T> toIntFunction, T[] values) {
        this.toIntFunction = toIntFunction;
        this.values = values;
        this.totalValue = new LazyLoader<>(this::gatherAll);
    }

    public T getRandom() {
        int total = totalValue.get();
        int weight = random.nextInt(total);
        for (int idx = values.length - 1; idx >= 0; idx--) {
            T t = values[idx];
            weight -= toIntFunction.applyAsInt(t);
            if (weight < 0) {
                return t;
            }
        }
        return null;
    }

    private int gatherAll() {
        int i = 0;
        for (T t : values)
            i += toIntFunction.applyAsInt(t);
        return i;
    }
}
