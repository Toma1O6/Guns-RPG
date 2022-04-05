package dev.toma.gunsrpg.util;

import java.util.function.Supplier;

public interface IIntervalProvider extends Supplier<Integer> {

    int getTicks();

    @Override
    default Integer get() {
        return this.getTicks();
    }
}
