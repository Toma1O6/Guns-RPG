package dev.toma.gunsrpg.util.object;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public static <R> Interaction<R> failure(final String message) {
        return failure(new StringTextComponent(message));
    }

    public <X extends Throwable> void validate(Function<ITextComponent, X> throwableConstructor) throws X {
        if (this.isFailure()) {
            throw throwableConstructor.apply(this.getMessage());
        }
    }

    public <X extends Throwable> R getOrThrow(Function<ITextComponent, X> throwableConstructor) throws X {
        if (this.isFailure()) {
            throw throwableConstructor.apply(this.getMessage());
        }
        return this.getResult();
    }

    public R getOrElse(R orElse) {
        return this.isFailure() ? orElse : this.getResult();
    }

    public Optional<R> optional() {
        return Optional.ofNullable(this.result);
    }

    public void ifSuccessOrElse(Consumer<R> success, Consumer<ITextComponent> failure) {
        if (this.isSuccessful())
            success.accept(this.getResult());
        else
            failure.accept(this.getMessage());
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
