package toma.config.object.builder;

import toma.config.datatypes.IConfigType;
import toma.config.datatypes.primitives.ConfigLong;

public class LongTypeBuilder extends AbstractNumberBuilder<Long, IConfigType<Long>> {

    public LongTypeBuilder(ConfigBuilder parent, long value) {
        super(parent, value);
        range = new Range<>(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Override
    protected IConfigType<Long> toConfigType() {
        return new ConfigLong(getName(), getComment(), getValue(), range.min(), range.max(), isSliderRender(), getAssignFunction());
    }
}
