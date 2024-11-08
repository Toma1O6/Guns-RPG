package dev.toma.gunsrpg.util.object;

import net.minecraft.util.text.ITextComponent;

import java.util.Objects;
import java.util.function.Consumer;

public final class Interaction<R> {

    private final Status status;
    private final R result;
    private final ITextComponent message;

    private Interaction(Status status, R result, ITextComponent message) {
        this.status = Objects.requireNonNull(status);
        this.result = result;
        this.message = message;
    }

    public static <R> Interaction<R> success(final R result) {
        return new Interaction<>(Status.SUCCESS, result, null);
    }

    public static <R> Interaction<R> failure(final ITextComponent message) {
        return new Interaction<>(Status.FAILURE, null, message);
    }

    public void on(Consumer<R> onSuccess, Consumer<ITextComponent> onFailure) {
        this.onSuccess(onSuccess);
        this.onFailure(onFailure);
    }

    public void onSuccess(Consumer<R> onSuccess) {
        if (this.isSuccessful()) {
            onSuccess.accept(this.result);
        }
    }

    public void onFailure(Consumer<ITextComponent> onFailure) {
        if (this.isFailure()) {
            onFailure.accept(this.message);
        }
    }

    public boolean isSuccessful() {
        return this.status == Status.SUCCESS;
    }

    public boolean isFailure() {
        return this.status == Status.FAILURE;
    }

    public R getResult() {
        return result;
    }

    public ITextComponent getMessage() {
        return message;
    }

    public <V> Interaction<V> failed() {
        return failure(this.message);
    }

    public enum Status {
        SUCCESS, FAILURE;
    }
}
