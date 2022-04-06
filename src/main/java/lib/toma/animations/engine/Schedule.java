package lib.toma.animations.engine;

import java.util.function.Supplier;

public final class Schedule<T> implements Supplier<T> {

    private int ticks;
    private final Supplier<T> provider;

    public Schedule(int ticks, Supplier<T> provider) {
        this(() -> ticks, provider);
    }

    public Schedule(Supplier<Integer> ticks, Supplier<T> provider) {
        this.ticks = ticks.get();
        this.provider = provider;
    }

    public void update() {
        --ticks;
    }

    public boolean done() {
        return ticks <= 0;
    }

    @Override
    public T get() {
        return provider.get();
    }
}
