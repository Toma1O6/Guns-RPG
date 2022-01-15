package dev.toma.gunsrpg.resource.gunner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

public final class DifficultyPropertyResolver {

    @SuppressWarnings("unchecked")
    public static <T> IDifficultyProperty<T> resolve(JsonElement element, IResolver<T> resolver) throws JsonSyntaxException {
        boolean simple = element.isJsonPrimitive();
        if (simple) {
            T t = resolver.resolve(element);
            return new SimpleDifficultyProperty<>(t);
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            T[] props = (T[]) new Object[array.size()];
            int index = 0;
            for (JsonElement jsonElement : array) {
                props[index++] = resolver.resolve(jsonElement);
            }
            return new MultiChoicePropertySelector<>(props);
        }
        throw new JsonSyntaxException("Property must be either primitive or primite array");
    }

    @FunctionalInterface
    public interface IResolver<T> {
        T resolve(JsonElement element) throws JsonSyntaxException;
    }

    private DifficultyPropertyResolver() {}
}
