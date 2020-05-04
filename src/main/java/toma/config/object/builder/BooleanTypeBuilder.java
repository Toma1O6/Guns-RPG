package toma.config.object.builder;

import toma.config.datatypes.IConfigType;
import toma.config.datatypes.primitives.ConfigBoolean;

public class BooleanTypeBuilder extends AbstractTypeBuilder<Boolean, IConfigType<Boolean>> {

    public BooleanTypeBuilder(ConfigBuilder parent, boolean value) {
        super(parent, value);
    }

    @Override
    public ConfigBoolean toConfigType() {
        return new ConfigBoolean(getName(), getComment(), getValue(), getAssignFunction());
    }
}
