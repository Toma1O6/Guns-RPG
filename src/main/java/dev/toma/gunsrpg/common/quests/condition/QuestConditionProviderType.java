package dev.toma.gunsrpg.common.quests.condition;

import net.minecraft.util.ResourceLocation;

public class QuestConditionProviderType<Q extends IQuestConditionProvider> {

    private final ResourceLocation id;
    private final IQuestConditionProviderSerializer<Q> serializer;
    private final boolean isFatal;

    QuestConditionProviderType(ResourceLocation id, IQuestConditionProviderSerializer<Q> serializer, boolean isFatal) {
        this.id = id;
        this.serializer = serializer;
        this.isFatal = isFatal;
    }
}
