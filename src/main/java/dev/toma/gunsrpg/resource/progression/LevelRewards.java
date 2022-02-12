package dev.toma.gunsrpg.resource.progression;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class LevelRewards {

    private static final Map<ResourceLocation, ILevelRewardAdapter<?>> ADAPTER_MAP = new HashMap<>();

    static {
        registerAdapter(GunsRPG.makeResource("point"), new SkillPointReward.Adapter());
        registerAdapter(GunsRPG.makeResource("item"), new ItemReward.Adapter());
    }

    public static void registerAdapter(ResourceLocation location, ILevelRewardAdapter<?> adapter) {
        ADAPTER_MAP.put(location, adapter);
    }

    @SuppressWarnings("unchecked")
    public static <R extends ILevelReward> ILevelRewardAdapter<R> getAdapter(ResourceLocation location) {
        return (ILevelRewardAdapter<R>) ADAPTER_MAP.get(location);
    }
}
