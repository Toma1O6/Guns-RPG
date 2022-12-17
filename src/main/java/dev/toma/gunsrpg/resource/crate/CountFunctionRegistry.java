package dev.toma.gunsrpg.resource.crate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.util.functions.IFunction;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class CountFunctionRegistry implements Codec<CountFunctionType<?>> {

    public static final CountFunctionType<ConstantCount> CONST = new CountFunctionType<>(GunsRPG.makeResource("const"), new ConstantCount.Adapter(), ConstantCount.CODEC);
    public static final CountFunctionType<RandomCount> RNG = new CountFunctionType<>(GunsRPG.makeResource("rng"), new RandomCount.Adapter(), RandomCount.CODEC);
    public static final CountFunctionRegistry INSTANCE = new CountFunctionRegistry();
    private final Map<ResourceLocation, CountFunctionType<?>> map = new HashMap<>();

    private CountFunctionRegistry() {
        registerFunction(CONST);
        registerFunction(RNG);
    }

    public synchronized void registerFunction(CountFunctionType<?> type) {
        if (map.put(type.getId(), type) != null) {
            throw new IllegalArgumentException("Duplicate function: " + type.getId());
        }
    }

    @SuppressWarnings("unchecked")
    public <F extends ICountFunction> CountFunctionType<F> findByKey(ResourceLocation identifier) {
        return (CountFunctionType<F>) map.get(identifier);
    }

    public <F extends ICountFunction> void encode(F function, PacketBuffer buffer) {
        ResourceLocation id = function.getFunctionType().getId();
        buffer.writeResourceLocation(id);
        CountFunctionType<F> type = findByKey(id);
        type.getAdapter().encode(function, buffer);
    }

    public <F extends ICountFunction> F decode(PacketBuffer buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        CountFunctionType<F> type = findByKey(id);
        return type.getAdapter().decode(buffer);
    }

    public <F extends ICountFunction> F fromJson(JsonElement element, IFunction range) {
        JsonObject object = JsonHelper.asJsonObject(element);
        String identifierString = JSONUtils.getAsString(object, "function", null);
        if (ModUtils.isNullOrEmpty(identifierString)) {
            throw new JsonParseException("Invalid property value: 'function' must be defined and it's value cannot be empty");
        }
        ResourceLocation identifier = new ResourceLocation(identifierString);
        CountFunctionType<F> type = findByKey(identifier);
        if (type == null) {
            throw new JsonParseException(String.format("Invalid property value 'function': '%s' is not valid function", identifierString));
        }
        return type.getAdapter().deserialize(object, range);
    }

    public <T> DataResult<T> encode(CountFunctionType<?> input, DynamicOps<T> ops, T prefix) {
        ResourceLocation key = input.getId();
        return key == null ? DataResult.error("Missing key for object " + input) : ops.mergeToPrimitive(prefix, ops.createString(key.toString()));
    }

    public <T> DataResult<Pair<CountFunctionType<?>, T>> decode(DynamicOps<T> ops, T input) {
        return ResourceLocation.CODEC.decode(ops, input).flatMap((pair) -> {
            CountFunctionType<?> value = findByKey(pair.getFirst());
            return value != null ? DataResult.success(Pair.of(value, pair.getSecond())) : DataResult.error("Unknown function key " + pair.getFirst());
        });
    }
}
