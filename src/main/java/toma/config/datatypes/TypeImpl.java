package toma.config.datatypes;

import com.google.gson.JsonObject;
import dev.toma.gunsrpg.config.GRPGConfig;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public abstract class TypeImpl<T> implements IConfigType<T> {

    private T value;
    private String comment;
    private String name;

    private Consumer<IConfigType<T>> assign;

    public TypeImpl(String name, String comment, @Nonnull T defaultValue, Consumer<IConfigType<T>> assign) {
        this.name = name;
        this.value = defaultValue;
        this.comment = comment;
        this.assign = assign;
    }

    @Override
    public boolean hasComment() {
        return comment != null;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String displayName() {
        return this.name;
    }

    @Override
    public void onLoad() {
        this.assign.accept(this);
    }

    public abstract void save(JsonObject toObj);

    public abstract void load(JsonObject fromObj);

    @Override
    public void set(T value) {
        this.value = value;
    }

    @Override
    public final T value() {
        return value;
    }

    @Override
    public String toString() {
        return displayName() + ": " + value().toString();
    }
}
