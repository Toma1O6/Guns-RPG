package dev.toma.gunsrpg.util.object;

import java.util.function.Supplier;

public class LazyLoader<T> implements Supplier<T> {

    private Supplier<T> supplier;
    private T value;

    public LazyLoader(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if(value == null) {
            value = supplier.get();
            supplier = null;
        }
        return value;
    }
}
