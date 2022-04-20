package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class QuestConditions {

    private static final Map<ResourceLocation, QuestConditionProviderType<?>> MAP = new HashMap<>();

    public static <Q extends IQuestConditionProvider> void register(ResourceLocation location, IQuestConditionProviderSerializer<Q> serializer) {
        QuestConditionProviderType<Q> type = new QuestConditionProviderType<>(location, serializer, false);
        MAP.put(location, type);
    }

    @SuppressWarnings("unchecked")
    public static <Q extends IQuestConditionProvider> QuestConditionProviderType<Q> getByKey(ResourceLocation key) {
        return (QuestConditionProviderType<Q>) MAP.get(key);
    }

    private static <Q extends IQuestConditionProvider> void register(String id, IQuestConditionProviderSerializer<Q> serializer) {
        register(GunsRPG.makeResource(id), serializer);
    }

    static {
        register("empty", new NoConditionProvider.Serializer());
    }
}
