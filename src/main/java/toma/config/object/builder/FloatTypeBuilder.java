package toma.config.object.builder;

import toma.config.datatypes.IConfigType;
import toma.config.datatypes.primitives.ConfigFloat;

public class FloatTypeBuilder extends AbstractNumberBuilder<Float, IConfigType<Float>> {

    public FloatTypeBuilder(ConfigBuilder parent, float value) {
        super(parent, value);
        range = new Range<>(Float.MIN_VALUE, Float.MAX_VALUE);
    }

    @Override
    protected IConfigType<Float> toConfigType() {
        return new ConfigFloat(getName(), getComment(), getValue(), range.min(), range.max(), isSliderRender(), getAssignFunction());
    }
}
