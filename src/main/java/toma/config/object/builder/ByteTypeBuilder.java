package toma.config.object.builder;

import toma.config.datatypes.IConfigType;
import toma.config.datatypes.primitives.ConfigByte;

public class ByteTypeBuilder extends AbstractNumberBuilder<Byte, IConfigType<Byte>> {

    public ByteTypeBuilder(ConfigBuilder parent, byte value) {
        super(parent, value);
        range = new Range<>(Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    @Override
    protected ConfigByte toConfigType() {
        return new ConfigByte(getName(), getComment(), getValue(), range.min(), range.max(), isSliderRender(), getAssignFunction());
    }
}
