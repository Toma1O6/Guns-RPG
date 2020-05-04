package toma.config.object.builder;

import toma.config.datatypes.ConfigEnum;
import toma.config.datatypes.IConfigType;

import java.util.Objects;

public class EnumTypeBuilder<T extends Enum<T>> extends AbstractTypeBuilder<T, IConfigType<T>> {

    private T[] arr;

    protected EnumTypeBuilder(ConfigBuilder parent, T value, T[] values) {
        super(parent, value);
        this.arr = values;
    }

    @Override
    protected IConfigType<T> toConfigType() {
        return new ConfigEnum<>(getName(), getComment(), getValue(), Objects.requireNonNull(arr), getAssignFunction());
    }
}
