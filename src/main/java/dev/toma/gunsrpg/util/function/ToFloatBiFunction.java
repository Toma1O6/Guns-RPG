package dev.toma.gunsrpg.util.function;

@FunctionalInterface
public interface ToFloatBiFunction<T, U> {

    float applyAsFloat(T t, U u);
}
