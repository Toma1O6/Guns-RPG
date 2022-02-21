package dev.toma.gunsrpg.resource.crate;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class CountFunctionRegistry {

    private static final Map<ResourceLocation, ICountFunctionAdapter> REGISTRY = new HashMap<>();

    public static void registerFunction(ResourceLocation identifier, ICountFunctionAdapter adapter) {
        REGISTRY.put(identifier, adapter);
    }

    public static ICountFunctionAdapter findByKey(ResourceLocation identifier) {
        return REGISTRY.get(identifier);
    }

    static {
        registerFunction(GunsRPG.makeResource("const"), new ConstantCount.Adapter());
        registerFunction(GunsRPG.makeResource("rng"), new RandomCount.Adapter());
    }
}
