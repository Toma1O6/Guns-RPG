package toma.config.object.builder;

import toma.config.datatypes.IConfigType;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractTypeBuilder<A, B extends IConfigType<A>> {

    private final ConfigBuilder parent;
    private String name;
    private String comment;
    private final A value;
    private Consumer<B> assignFunction = t -> {};

    protected AbstractTypeBuilder(ConfigBuilder parent, A value) {
        this.parent = parent;
        this.value = value;
    }

    public AbstractTypeBuilder<A, B> name(String name) {
        this.name = name;
        return this;
    }

    public AbstractTypeBuilder<A, B> comment(String comment) {
        this.comment = comment;
        return this;
    }

    public AbstractTypeBuilder<A, B> assign(Consumer<B> assignFunction) {
        this.assignFunction = assignFunction;
        return this;
    }

    public ConfigBuilder add() {
        Objects.requireNonNull(name, "Name cannot be null!");
        Objects.requireNonNull(value, "Value cannot be null!");
        this.parent.mainObject.put(this.toConfigType());
        return parent;
    }

    public ConfigBuilder add(Consumer<B> assignFunction) {
        this.assign(assignFunction);
        return add();
    }

    protected abstract B toConfigType();

    protected String getName() {
        return name;
    }

    protected String getComment() {
        return comment;
    }

    protected Consumer<B> getAssignFunction() {
        return assignFunction;
    }

    protected A getValue() {
        return value;
    }
}
