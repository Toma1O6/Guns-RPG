package toma.config.object.builder;

import toma.config.datatypes.IConfigType;
import toma.config.datatypes.primitives.ConfigShort;

public class ShortTypeBuilder extends AbstractNumberBuilder<Short, IConfigType<Short>> {

    public ShortTypeBuilder(ConfigBuilder builder, short value) {
        super(builder, value);
        this.range = new Range<>(Short.MIN_VALUE, Short.MAX_VALUE);
    }

    @Override
    protected IConfigType<Short> toConfigType() {
        return new ConfigShort(getName(), getComment(), getValue(), range.min(), range.max(), isSliderRender(), getAssignFunction());
    }
}
