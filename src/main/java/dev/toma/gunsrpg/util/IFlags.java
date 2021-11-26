package dev.toma.gunsrpg.util;

import java.util.function.Function;

public interface IFlags {

    boolean is(int flags);

    @SafeVarargs
    static <T> int combine(Function<T, Integer> value, T... values) {
        int total = 0;
        for (T flags : values) {
            total |= value.apply(flags);
        }
        return total;
    }

    static int value(int index) {
        return 1 << index;
    }
}
