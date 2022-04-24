package dev.toma.gunsrpg.common.quests.quest;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class QuestTypes {

    private static final Map<ResourceLocation, QuestType<?>> MAP = new HashMap<>();

    public static <D extends IQuestData> QuestType<D> register(ResourceLocation id, QuestType.IQuestDataResolver<D> resolver) {
        QuestType<D> type = new QuestType<>(id, resolver);
        MAP.put(id, type);
        return type;
    }

    @SuppressWarnings("unchecked")
    public <D extends IQuestData> QuestType<D> getTypeById(ResourceLocation id) {
        return (QuestType<D>) MAP.get(id);
    }
}
