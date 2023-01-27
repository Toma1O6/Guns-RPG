package dev.toma.gunsrpg.util.math;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.util.object.LazyLoader;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> WeightedRandom<Entry<T>> fromEntries(List<Entry<T>> entries) {
        return new WeightedRandom<>(Entry::getWeight, entries.toArray(new Entry[0]));
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

    public T[] getValues() {
        return values;
    }

    public int getValueCount() {
        return values.length;
    }

    private int gatherAll() {
        int i = 0;
        for (T t : values)
            i += toIntFunction.applyAsInt(t);
        return i;
    }

    public static final class Entry<T> implements Supplier<T>, ToIntFunction<T> {

        private final T value;
        private final int weight;

        public Entry(T value, int weight) {
            this.value = value;
            this.weight = weight;
        }

        public static <T> Codec<Entry<T>> codec(Codec<T> elementCodec) {
            return RecordCodecBuilder.create(instance -> instance.group(
                    elementCodec.fieldOf("value").forGetter(Entry::getValue),
                    Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("weight", 1).forGetter(Entry::getWeight)
            ).apply(instance, Entry::new));
        }

        @Override
        public T get() {
            return this.getValue();
        }

        @Override
        public int applyAsInt(T value) {
            return this.weight;
        }

        public T getValue() {
            return value;
        }

        public int getWeight() {
            return weight;
        }
    }
}
