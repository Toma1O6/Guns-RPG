package dev.toma.gunsrpg.util.helper;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Predicate;

public final class JsonHelper {

    public static JsonElement get(JsonObject source, String key) {
        if (!source.has(key)) {
            throw new JsonSyntaxException("Missing " + key + " attribute");
        }
        return source.get(key);
    }

    public static <R> R[] deserializeInto(JsonArray array, Function<Integer, R[]> arrayFactory, Function<JsonElement, R> adapter) {
        int index = 0;
        R[] output = arrayFactory.apply(array.size());
        for (JsonElement element : array) {
            output[index++] = adapter.apply(element);
        }
        return output;
    }

    public static Item resolveItem(JsonElement element) throws JsonParseException {
        ResourceLocation itemId = new ResourceLocation(element.getAsString());
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        if (item == null || item == Items.AIR) throw new JsonSyntaxException("Unknown item: " + itemId);
        return item;
    }

    public static JsonElement toSimpleJson(ResourceLocation location) {
        return location == null ? JsonNull.INSTANCE : new JsonPrimitive(location.toString());
    }

    public static JsonObject asJsonObject(JsonElement element) throws JsonParseException {
        return as(element, JsonElement::isJsonObject, JsonElement::getAsJsonObject, "JsonObject");
    }

    public static JsonArray asJsonArray(JsonElement element) throws JsonParseException {
        return as(element, JsonElement::isJsonArray, JsonElement::getAsJsonArray, "JsonArray");
    }

    private static <J> J as(JsonElement element, Predicate<JsonElement> condition, Function<JsonElement, J> converter, String type) {
        if (!condition.test(element)) {
            throw new JsonSyntaxException("Not a " + type + ", got " + element.getClass().getName());
        }
        return converter.apply(element);
    }

    private JsonHelper() {}
}
