package toma.config.object.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import toma.config.datatypes.IConfigType;
import toma.config.datatypes.list.ConfigList;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ListTypeBuilder<T> extends AbstractTypeBuilder<List<T>, IConfigType<List<T>>> {

    private Consumer<IConfigType<List<T>>> assign;
    private BiConsumer<JsonArray, T> save;
    private Function<JsonElement, T> load;
    private Supplier<T> factory;
    private boolean fixedSize;

    public ListTypeBuilder(ConfigBuilder builder, List<T> value) {
        super(builder, value);
    }

    @Override
    public ListTypeBuilder<T> name(String name) {
        super.name(name);
        return this;
    }

    @Override
    public ListTypeBuilder<T> comment(String comment) {
        super.comment(comment);
        return this;
    }

    public ListTypeBuilder<T> constantSize() {
        this.fixedSize = true;
        return this;
    }

    @Override
    public ListTypeBuilder<T> assign(Consumer<IConfigType<List<T>>> assign) {
        this.assign = assign;
        return this;
    }

    public ListTypeBuilder<T> saveElement(BiConsumer<JsonArray, T> saveFunction) {
        this.save = saveFunction;
        return this;
    }

    public ListTypeBuilder<T> loadElement(Function<JsonElement, T> loader) {
        this.load = loader;
        return this;
    }

    public ListTypeBuilder<T> factory(Supplier<T> factory) {
        this.factory = factory;
        return this;
    }

    @Override
    protected IConfigType<List<T>> toConfigType() {
        return new ConfigList<>(
                getName(),
                getComment(),
                getValue(),
                fixedSize,
                assign,
                Objects.requireNonNull(save, "Not building: Save function not provided"),
                Objects.requireNonNull(load, "Not building: Load function not provided"),
                fixedSize ? Objects.requireNonNull(factory, "Not building: Object factory not provided!") : factory
        );
    }
}
