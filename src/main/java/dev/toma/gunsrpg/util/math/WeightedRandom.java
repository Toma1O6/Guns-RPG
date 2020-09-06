package dev.toma.gunsrpg.util.math;

import dev.toma.gunsrpg.util.object.LazyLoader;

import java.util.Random;
import java.util.function.Function;

public class WeightedRandom<T> {

    protected static Random random = new Random();
    protected final T[] values;
    protected final Function<T, Integer> typeToValueFunction;
    protected final LazyLoader<Integer> totalValue;

    public WeightedRandom(Function<T, Integer> function, T[] values) {
        this.typeToValueFunction = function;
        this.values = values;
        this.totalValue = new LazyLoader<>(this::gatherAll);
    }

    public T getRandom() {
        int total = totalValue.get();
        int weight = random.nextInt(total);
        for(int idx = values.length - 1; idx >= 0; idx--) {
            T t = values[idx];
            weight -= typeToValueFunction.apply(t);
            if(weight < 0) {
                return t;
            }
        }
        return null;
    }

    private int gatherAll() {
        int i = 0;
        for(T t : values)
            i += typeToValueFunction.apply(t);
        return i;
    }
}
