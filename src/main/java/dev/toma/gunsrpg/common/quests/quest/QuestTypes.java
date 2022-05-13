package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.GunsRPG;
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
    public static <D extends IQuestData> QuestType<D> getTypeById(ResourceLocation id) {
        return (QuestType<D>) MAP.get(id);
    }

    static {
        register(GunsRPG.makeResource("kill_entities"), new KillEntityData.Serializer());
        register(GunsRPG.makeResource("kill_in_area"), new KillInAreaData.Serializer());
        register(GunsRPG.makeResource("survival"), new SurvivalData.Serializer());
        register(GunsRPG.makeResource("area_survival"), new AreaSurvivalData.Serializer());
        register(GunsRPG.makeResource("handover"), new ItemHandoverData.Serializer());
    }
}
