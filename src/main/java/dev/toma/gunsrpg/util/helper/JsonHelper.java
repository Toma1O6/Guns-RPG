package dev.toma.gunsrpg.util.helper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.util.function.Function;
import java.util.function.Predicate;

public final class JsonHelper {

    public static JsonObject asJsonObject(JsonElement element) throws JsonParseException {
        return as(element, JsonElement::isJsonObject, JsonElement::getAsJsonObject, "JsonObject");
    }

    public static JsonObject asJsonArray(JsonElement element) throws JsonParseException {
        return as(element, JsonElement::isJsonArray, JsonHelper::asJsonArray, "JsonArray");
    }

    private static <J> J as(JsonElement element, Predicate<JsonElement> condition, Function<JsonElement, J> converter, String type) {
        if (!condition.test(element)) {
            throw new JsonSyntaxException("Not a " + type + ", got " + element.getClass().getName());
        }
        return converter.apply(element);
    }

    private JsonHelper() {}
}
