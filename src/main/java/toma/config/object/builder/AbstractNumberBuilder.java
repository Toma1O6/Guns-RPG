package toma.config.object.builder;

import toma.config.datatypes.IConfigType;

import java.util.Objects;

public abstract class AbstractNumberBuilder<A extends Number, B extends IConfigType<A>> extends AbstractTypeBuilder<A, B> {

    protected Range<A> range;
    private boolean sliderRender;

    public AbstractNumberBuilder(ConfigBuilder parent, A value) {
        super(parent, value);
    }

    @Override
    public AbstractNumberBuilder<A, B> name(String name) {
        super.name(name);
        return this;
    }

    @Override
    public AbstractNumberBuilder<A, B> comment(String comment) {
        super.comment(comment);
        return this;
    }

    public AbstractNumberBuilder<A, B> sliderRendering() {
        this.sliderRender = true;
        return this;
    }

    public AbstractNumberBuilder<A, B> range(A min, A max) {
        this.range = new Range<>(min, max);
        return this;
    }

    public AbstractNumberBuilder<A, B> range(Range<A> range) {
        this.range = range;
        return this;
    }

    @Override
    public ConfigBuilder add() {
        Objects.requireNonNull(range, "Number range cannot be null!");
        return super.add();
    }

    protected boolean isSliderRender() {
        return sliderRender;
    }

    public static class Range<N extends Number> {

        private final N min, max;

        public Range(N min, N max) {
            this.min = min;
            this.max = max;
        }

        public N min() {
            return min;
        }

        public N max() {
            return max;
        }
    }
}
