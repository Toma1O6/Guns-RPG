package dev.toma.gunsrpg.util.locate;

import java.util.stream.Stream;

public interface ILocator<T, CTX> {

    T locateFirst(CTX context, IContextIterator<T> iterator, ILocatorPredicate<T> predicate);

    Stream<T> locateAll(CTX context, IContextIterator<T> iterator, ILocatorPredicate<T> predicate);
}
