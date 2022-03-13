package dev.toma.gunsrpg.util.locate;

import java.util.Iterator;

public final class SimpleIterator<T> implements IContextIterator<T> {

    private final Iterator<T> iterator;

    public SimpleIterator(Iterable<T> iterable) {
        this.iterator = iterable.iterator();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
}
