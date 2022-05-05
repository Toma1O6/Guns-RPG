package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class QuestConditions {

    private static final Map<ResourceLocation, QuestConditionProviderType<?>> MAP = new HashMap<>();
    public static final QuestConditionProviderType<NoConditionProvider> NO_CONDITION_TYPE;

    public static <Q extends IQuestConditionProvider> QuestConditionProviderType<Q> register(ResourceLocation location, IQuestConditionProviderSerializer<Q> serializer) {
        QuestConditionProviderType<Q> type = new QuestConditionProviderType<>(location, serializer, false);
        MAP.put(location, type);
        return type;
    }

    @SuppressWarnings("unchecked")
    public static <Q extends IQuestConditionProvider> QuestConditionProviderType<Q> getByKey(ResourceLocation key) {
        return (QuestConditionProviderType<Q>) MAP.get(key);
    }

    private static <Q extends IQuestConditionProvider> QuestConditionProviderType<Q> register(String id, IQuestConditionProviderSerializer<Q> serializer) {
        return register(GunsRPG.makeResource(id), serializer);
    }

    static {
        NO_CONDITION_TYPE = register("empty", new NoConditionProvider.Serializer());
        register("used_item", new SpecificWeaponConditionProvider.Serializer());
        register("debuff", new ActiveDebuffConditionProvider.Serializer());
        register("no_food", new NoFoodConditionProvider.Serializer());
        register("no_heal", new NoHealConditionProvider.Serializer());
        register("explode", new ExplodeConditionProvider.Serializer());
        register("equipment", new EquipmentConditionProvider.Serializer());
    }
}
