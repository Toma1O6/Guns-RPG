package toma.config.object.builder;

import toma.config.datatypes.IConfigType;
import toma.config.datatypes.primitives.ConfigDouble;

public class DoubleTypeBuilder extends AbstractNumberBuilder<Double, IConfigType<Double>> {

    public DoubleTypeBuilder(ConfigBuilder builder, double value) {
        super(builder, value);
        range = new Range<>(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    protected IConfigType<Double> toConfigType() {
        return new ConfigDouble(getName(), getComment(), getValue(), range.min(), range.max(), isSliderRender(), getAssignFunction());
    }
}
