package dev.toma.gunsrpg.util.locate;

import java.util.function.Predicate;

@FunctionalInterface
public interface ILocatorPredicate<T> extends Predicate<T> {

    boolean isValidResult(T t);

    @Override
    default boolean test(T t) {
        return isValidResult(t);
    }
}
