package dev.toma.gunsrpg.util.function;

@FunctionalInterface
public interface ToFloatFunction<T> {

    float applyAsFloat(T t);
}
