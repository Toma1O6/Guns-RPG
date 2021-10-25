package dev.toma.gunsrpg.util.locate;

@FunctionalInterface
public interface IContextIterator<T> {

    T next();
}
