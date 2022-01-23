package dev.toma.gunsrpg.util.locate;

public interface IContextIterator<T> {

    T next();

    boolean hasNext();
}
