package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public final class QuestTypes {

    private static final Map<ResourceLocation, QuestType<?, ?>> MAP = new HashMap<>();

    public static <D extends IQuestData, Q extends Quest<D>> QuestType<D, Q> register(ResourceLocation id, QuestType.IQuestDataResolver<D> resolver, IQuestFactory<D, Q> factory) {
        QuestType<D, Q> type = new QuestType<>(id, resolver, factory);
        MAP.put(id, type);
        return type;
    }

    @SuppressWarnings("unchecked")
    public static <D extends IQuestData, Q extends Quest<D>> QuestType<D, Q> getTypeById(ResourceLocation id) {
        return (QuestType<D, Q>) MAP.get(id);
    }

    @SuppressWarnings("unchecked")
    public static <D extends IQuestData, Q extends Quest<D>> Q getFromNbt(World world, CompoundNBT nbt) {
        QuestDeserializationContext<D> context = QuestDeserializationContext.fromNbt(world, nbt);
        QuestType<D, Q> type = (QuestType<D, Q>) context.getScheme().getQuestType();
        Q quest = type.fromContext(context);
        quest.readQuestData(context.getInternalData());
        return quest;
    }

    static {
        register(GunsRPG.makeResource("kill_entities"), new KillEntityData.Serializer(), KillEntitiesQuest.FACTORY);
        register(GunsRPG.makeResource("kill_in_area"), new KillInAreaData.Serializer(), KillInAreaQuest.FACTORY);
        register(GunsRPG.makeResource("survival"), new SurvivalData.Serializer(), SurvivalQuest.FACTORY);
        register(GunsRPG.makeResource("area_survival"), new AreaSurvivalData.Serializer(), AreaSurvivalQuest.FACTORY);
        register(GunsRPG.makeResource("handover"), new ItemHandoverData.Serializer(), ItemHandoverQuest.FACTORY);
    }
}
