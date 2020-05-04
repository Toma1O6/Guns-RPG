package toma.config.object.builder;

import toma.config.datatypes.IConfigType;
import toma.config.datatypes.primitives.ConfigInteger;

public class IntTypeBuilder extends AbstractNumberBuilder<Integer, IConfigType<Integer>> {

    public IntTypeBuilder(ConfigBuilder parent, int value) {
        super(parent, value);
        this.range = new Range<>(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    protected IConfigType<Integer> toConfigType() {
        return new ConfigInteger(getName(), getComment(), getValue(), range.min(), range.max(), isSliderRender(), getAssignFunction());
    }
}
