package toma.config.object.builder;

import toma.config.datatypes.IConfigType;
import toma.config.datatypes.primitives.ConfigString;

public class StringTypeBuilder extends AbstractTypeBuilder<String, IConfigType<String>> {

    public StringTypeBuilder(ConfigBuilder builder, String value) {
        super(builder, value);
    }

    @Override
    protected IConfigType<String> toConfigType() {
        return new ConfigString(getName(), getComment(), getValue(), getAssignFunction());
    }
}
