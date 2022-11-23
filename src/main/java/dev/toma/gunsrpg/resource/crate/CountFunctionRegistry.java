package dev.toma.gunsrpg.resource.crate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.util.functions.IFunction;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class CountFunctionRegistry {

    public static final ResourceLocation CONST = GunsRPG.makeResource("const");
    public static final ResourceLocation RNG = GunsRPG.makeResource("rng");
    private static final Map<ResourceLocation, ICountFunctionAdapter<?>> REGISTRY = new HashMap<>();

    public static void registerFunction(ResourceLocation identifier, ICountFunctionAdapter<?> adapter) {
        REGISTRY.put(identifier, adapter);
    }

    @SuppressWarnings("unchecked")
    public static <F extends ICountFunction> ICountFunctionAdapter<F> findByKey(ResourceLocation identifier) {
        return (ICountFunctionAdapter<F>) REGISTRY.get(identifier);
    }

    public static <F extends ICountFunction> void encode(F function, PacketBuffer buffer) {
        ResourceLocation id = function.getId();
        buffer.writeResourceLocation(id);
        ICountFunctionAdapter<F> adapter = findByKey(id);
        adapter.encode(function, buffer);
    }

    public static <F extends ICountFunction> F decode(PacketBuffer buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        ICountFunctionAdapter<F> adapter = findByKey(id);
        return adapter.decode(buffer);
    }

    public static <F extends ICountFunction> F fromJson(JsonElement element, IFunction range) {
        JsonObject object = JsonHelper.asJsonObject(element);
        String identifierString = JSONUtils.getAsString(object, "function", null);
        if (ModUtils.isNullOrEmpty(identifierString)) {
            throw new JsonParseException("Invalid property value: 'function' must be defined and it's value cannot be empty");
        }
        ResourceLocation identifier = new ResourceLocation(identifierString);
        ICountFunctionAdapter<F> adapter = CountFunctionRegistry.findByKey(identifier);
        if (adapter == null) {
            throw new JsonParseException(String.format("Invalid property value 'function': '%s' is not valid function", identifierString));
        }
        return adapter.deserialize(object, range);
    }

    static {
        registerFunction(CONST, new ConstantCount.Adapter());
        registerFunction(RNG, new RandomCount.Adapter());
    }
}
