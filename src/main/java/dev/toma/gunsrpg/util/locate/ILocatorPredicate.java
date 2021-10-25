package dev.toma.gunsrpg.util.locate;

@FunctionalInterface
public interface ILocatorPredicate<T> {

    boolean isValidResult(T t);
}
