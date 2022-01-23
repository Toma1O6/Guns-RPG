package dev.toma.gunsrpg.util.locate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractLocator<T, CTX> implements ILocator<T, CTX> {

    private final T fallbackValue;

    public AbstractLocator(T fallbackValue) {
        this.fallbackValue = fallbackValue;
    }

    @Override
    public T locateFirst(CTX context, IContextIterator<T> iterator, ILocatorPredicate<T> predicate) {
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.isValidResult(t)) {
                return t;
            }
        }
        return fallbackValue;
    }

    @Override
    public Stream<T> locateAll(CTX context, IContextIterator<T> iterator, ILocatorPredicate<T> predicate) {
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.isValidResult(t)) {
                list.add(t);
            }
        }
        return list.stream();
    }
}
