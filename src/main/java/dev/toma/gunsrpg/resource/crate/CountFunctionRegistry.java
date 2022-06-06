package dev.toma.gunsrpg.resource.crate;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.network.PacketBuffer;
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

    static {
        registerFunction(CONST, new ConstantCount.Adapter());
        registerFunction(RNG, new RandomCount.Adapter());
    }
}
