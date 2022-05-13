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
        NO_CONDITION_TYPE = register("empty", SimpleProviderSerializer.withConstantResult(() -> NoConditionProvider.NO_CONDITION));
        register("used_item", new SpecificWeaponConditionProvider.Serializer());
        register("debuff", new ActiveDebuffConditionProvider.Serializer());
        register("no_food", SimpleProviderSerializer.withResultOf(NoFoodConditionProvider::new));
        register("no_heal", SimpleProviderSerializer.withResultOf(NoHealConditionProvider::new));
        register("explode", SimpleProviderSerializer.withResultOf(ExplodeConditionProvider::new));
        register("equipment", new EquipmentConditionProvider.Serializer());
        register("aggro", new HasAggroConditionProvider.Serializer());
        register("distance", new DistanceConditionProvider.Serializer());
        register("no_damage_taken", SimpleProviderSerializer.withResultOf(NoDamageTakenConditionProvider::new));
        register("no_damage_given", SimpleProviderSerializer.withResultOf(NoDamageGivenConditionProvider::new));
        register("is_headshot", new HeadshotConditionProvider.Serializer());
        register("unique", SimpleProviderSerializer.withResultOf(UniqueMobKillsConditionProvider::new));
    }
}
