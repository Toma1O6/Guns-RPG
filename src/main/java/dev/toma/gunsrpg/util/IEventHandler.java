package dev.toma.gunsrpg.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IEventHandler<T> {

    void invoke(Consumer<T> consumer);

    void invokeIf(Predicate<T> condition, Consumer<T> action);

    void addListener(T t);

    void removeListener(T t);

    List<T> listAll();

    static <T> IEventHandler<T> newEventHandler() {
        return new EventHandler<>();
    }

    class EventHandler<T> implements IEventHandler<T> {

        private final List<T> list = new ArrayList<>();

        private EventHandler() {}

        @Override
        public void invoke(Consumer<T> consumer) {
            list.forEach(consumer);
        }

        @Override
        public void invokeIf(Predicate<T> condition, Consumer<T> action) {
            list.stream().filter(condition).forEach(action);
        }

        @Override
        public void addListener(T t) {
            list.add(t);
        }

        @Override
        public void removeListener(T t) {
            list.remove(t);
        }

        @Override
        public List<T> listAll() {
            return list;
        }
    }
}
