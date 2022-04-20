package dev.toma.gunsrpg.common.quests.reward;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class AssemblyFunctions {

    private static final Map<ResourceLocation, IAssemblyFunctionSerializer> MAP = new HashMap<>();

    public static void register(ResourceLocation id, IAssemblyFunctionSerializer serializer) {
        MAP.put(id, serializer);
    }

    public static IAssemblyFunctionSerializer getSerializer(ResourceLocation id) {
        return MAP.get(id);
    }

    static {
        register(GunsRPG.makeResource("crystal"), new CrystalAssemblyFunction.Serializer());
    }
}
