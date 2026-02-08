package dev.toma.gunsrpg.util.helper;

import com.google.gson.*;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

    public static <C, R> C deserialize(JsonArray array, Function<JsonArray, C> constructor, Function<JsonElement, R> parser, BiConsumer<C, R> accumulator) {
        C storage = constructor.apply(array);
        for (JsonElement element : array) {
            R result = parser.apply(element);
            accumulator.accept(storage, result);
        }
        return storage;
    }

    public static <R> List<R> deserializeAsList(String arrayKey, JsonObject object, Function<JsonElement, R> parser) {
        return object.has(arrayKey) ? deserialize(JSONUtils.getAsJsonArray(object, arrayKey), arr -> new ArrayList<>(), parser, List::add) : Collections.emptyList();
    }

    public static <R> List<R> deserializeAsList(JsonArray array, Function<JsonElement, R> parser) {
        List<R> list = new ArrayList<>();
        for (JsonElement element : array) {
            list.add(parser.apply(element));
        }
        return list;
    }

    public static Item resolveItem(JsonElement element) throws JsonParseException {
        return resolveRegistryEntry(element, ForgeRegistries.ITEMS);
    }

    public static EntityType<?> resolveEntityType(JsonElement element) throws JsonParseException {
        return resolveRegistryEntry(element, ForgeRegistries.ENTITIES);
    }

    public static SkillType<?> resolveSkill(JsonElement element) throws JsonParseException {
        return resolveRegistryEntry(element, ModRegistries.SKILLS);
    }

    public static <T extends IForgeRegistryEntry<T>> T resolveRegistryEntry(JsonElement element, IForgeRegistry<T> registry) {
        ResourceLocation entryId = new ResourceLocation(element.getAsString());
        if (!registry.containsKey(entryId)) {
            throw new JsonSyntaxException(String.format(Locale.ROOT, "Unable to find \"%s\" item in registry %s", entryId, registry.getRegistryName()));
        }
        return registry.getValue(entryId);
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

    public static int getAsBoundedInt(JsonObject object, String key, int fallbackValue, int lowerBound, int upperBound) {
        int val = JSONUtils.getAsInt(object, key, fallbackValue);
        if (val < lowerBound) {
            throw new JsonSyntaxException(key + " must not be smaller than " + lowerBound + ", got " + val);
        }
        if (val > upperBound) {
            throw new JsonSyntaxException(key + " must not be bigger than " + upperBound + ", got" + val);
        }
        return val;
    }

    public static <T> Optional<T> optionally(JsonObject object, String key, BiFunction<JsonObject, String, T> function) {
        return object.has(key) ? Optional.of(function.apply(object, key)) : Optional.empty();
    }

    private static <J> J as(JsonElement element, Predicate<JsonElement> condition, Function<JsonElement, J> converter, String type) {
        if (!condition.test(element)) {
            throw new JsonSyntaxException("Not a " + type + ", got " + element.getClass().getName());
        }
        return converter.apply(element);
    }

    private JsonHelper() {}
}
