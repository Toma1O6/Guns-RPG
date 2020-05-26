package dev.toma.gunsrpg.util.object;

import java.util.function.Consumer;

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

    public void put(T t) {
        this.t = t;
    }

    public void ifPresent(Consumer<T> action) {
        if(t != null) {
            action.accept(t);
        }
    }

    public void clear() {
        this.t = null;
    }
}
