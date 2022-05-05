package dev.toma.gunsrpg.util.helper;

import com.mojang.brigadier.context.CommandContext;

import java.util.Optional;
import java.util.function.BiFunction;

public final class CommandHelper {

    public static <T, S>Optional<T> getArgumentOptional(CommandContext<S> context, String key, BiFunction<CommandContext<S>, String, T> getter) {
        T t = null;
        try {
            t = getter.apply(context, key);
        } catch (IllegalArgumentException ignored) {
        }
        return Optional.ofNullable(t);
    }

    private CommandHelper() {}
}
