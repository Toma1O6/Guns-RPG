package dev.toma.gunsrpg.util.object;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class OptionalObject<T> {

    private T t;

    private OptionalObject(T t) {
        this.t = t;
    }

    public static <T> OptionalObject<T> of(T t) {
        return new OptionalObject<>(t);
    }

    public static <T> OptionalObject<T> empty() {
        return new OptionalObject<>(null);
    }

    public boolean isPresent() {
        return t != null;
    }

    public T get() {
        return t;
    }

    public void map(T t) {
        this.t = t;
    }

    public T orMap(T value) {
        if (t == null) {
            map(value);
        }
        return t;
    }

    public T or(T value) {
        if (t == null) return value;
        else return t;
    }

    public <EX extends Exception> T orThrow(Supplier<EX> exceptionSupplier) throws EX {
        if (t != null) {
            return t;
        } else throw exceptionSupplier.get();
    }

    public void ifPresent(Consumer<T> action) {
        if (t != null) {
            action.accept(t);
        }
    }

    public void clear() {
        this.t = null;
    }
}
