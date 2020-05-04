package toma.config.object.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import toma.config.ConfigSubcategory;
import toma.config.datatypes.ConfigObject;
import toma.config.util.ConfigUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ConfigBuilder {

    protected ConfigBuilder parent;
    protected ConfigObject mainObject;

    private ConfigBuilder(ConfigObject mainObject, ConfigBuilder parent) {
        this.mainObject = mainObject;
        this.parent = parent;
    }

    public static ConfigBuilder create(ConfigObject mainObject) {
        return new ConfigBuilder(mainObject, null);
    }

    public BooleanTypeBuilder addBoolean(boolean value) {
        return new BooleanTypeBuilder(this, value);
    }

    public ByteTypeBuilder addByte(byte value) {
        return new ByteTypeBuilder(this, value);
    }

    public ShortTypeBuilder addShort(short value) {
        return new ShortTypeBuilder(this, value);
    }

    public IntTypeBuilder addInt(int value) {
        return new IntTypeBuilder(this, value);
    }

    public LongTypeBuilder addLong(long value) {
        return new LongTypeBuilder(this, value);
    }

    public FloatTypeBuilder addFloat(float value) {
        return new FloatTypeBuilder(this, value);
    }

    public DoubleTypeBuilder addDouble(double value) {
        return new DoubleTypeBuilder(this, value);
    }

    public StringTypeBuilder addString(String value) {
        return new StringTypeBuilder(this, value);
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> EnumTypeBuilder<T> addEnum(T value) {
        return new EnumTypeBuilder<>(this, value, (T[]) value.getClass().getEnumConstants());
    }

    public <T> ListTypeBuilder<T> list(List<T> value) {
        return new ListTypeBuilder<>(this, value);
    }

    public <T> ListTypeBuilder<T> list(T... values) {
        return list(ConfigUtils.makeList(values));
    }

    public ListTypeBuilder<Boolean> booleanList(List<Boolean> value) {
        return list(value).saveElement(JsonArray::add).loadElement(JsonElement::getAsBoolean).factory(() -> false);
    }

    public ListTypeBuilder<Boolean> booleanList(Boolean... values) {
        return booleanList(ConfigUtils.makeList(values));
    }

    public ListTypeBuilder<Byte> byteList(List<Byte> value) {
        return list(value).saveElement(JsonArray::add).loadElement(JsonElement::getAsByte).factory(() -> (byte) 0);
    }

    public ListTypeBuilder<Byte> byteList(Byte... values) {
        return byteList(ConfigUtils.makeList(values));
    }

    public ListTypeBuilder<Short> shortList(List<Short> value) {
        return list(value).saveElement(JsonArray::add).loadElement(JsonElement::getAsShort).factory(() -> (short) 0);
    }

    public ListTypeBuilder<Short> shortList(Short... values) {
        return shortList(ConfigUtils.makeList(values));
    }

    public ListTypeBuilder<Integer> intList(List<Integer> value) {
        return list(value).saveElement(JsonArray::add).loadElement(JsonElement::getAsInt).factory(() -> 0);
    }

    public ListTypeBuilder<Integer> intList(Integer... values) {
        return intList(ConfigUtils.makeList(values));
    }

    public ListTypeBuilder<Long> longList(List<Long> value) {
        return list(value).saveElement(JsonArray::add).loadElement(JsonElement::getAsLong).factory(() -> 0L);
    }

    public ListTypeBuilder<Long> longList(Long... values) {
        return longList(ConfigUtils.makeList(values));
    }

    public ListTypeBuilder<Float> floatList(List<Float> value) {
        return list(value).saveElement(JsonArray::add).loadElement(JsonElement::getAsFloat).factory(() -> 0.0F);
    }

    public ListTypeBuilder<Float> floatList(Float... values) {
        return floatList(ConfigUtils.makeList(values));
    }

    public ListTypeBuilder<Double> doubleList(List<Double> value) {
        return list(value).saveElement(JsonArray::add).loadElement(JsonElement::getAsDouble).factory(() -> 0.0D);
    }

    public ListTypeBuilder<Double> doubleList(Double... values) {
        return doubleList(ConfigUtils.makeList(values));
    }

    public ListTypeBuilder<String> stringList(List<String> value) {
        return list(value).saveElement(JsonArray::add).loadElement(JsonElement::getAsString).factory(() -> "");
    }

    public ListTypeBuilder<String> stringList(String... values) {
        return stringList(ConfigUtils.makeList(values));
    }

    public ObjectTypeBuilder push() {
        return new ObjectTypeBuilder(this);
    }

    public ConfigBuilder pop() {
        return this.parent;
    }

    public ConfigBuilder exec(Function<ConfigBuilder, ConfigBuilder> function) {
        return function.apply(this);
    }

    public ConfigBuilder exec(ConfigSubcategory configSubcategory) {
        return configSubcategory.toConfigFormat(this);
    }

    public ConfigObject build() {
        return mainObject;
    }

    public static class ObjectTypeBuilder {

        private String name, comment;
        private final ConfigBuilder parent;

        public ObjectTypeBuilder(ConfigBuilder parent) {
            this.parent = parent;
        }

        public ObjectTypeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ObjectTypeBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public ConfigBuilder init() {
            ConfigObject object = new ConfigObject(Objects.requireNonNull(name, "Name cannot be null!"), comment);
            parent.mainObject.add(name, object);
            return new ConfigBuilder(object, parent);
        }
    }
}
