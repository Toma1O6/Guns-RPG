package dev.toma.gunsrpg.util.locate;

import java.util.stream.Stream;

public class IterableLocator<T> extends AbstractLocator<T, Iterable<T>> {

    public IterableLocator(T fallbackValue) {
        super(fallbackValue);
    }

    public Stream<T> locateAll(Iterable<T> iterable, ILocatorPredicate<T> predicate) {
        return locateAll(iterable, new SimpleIterator<>(iterable), predicate);
    }
}
