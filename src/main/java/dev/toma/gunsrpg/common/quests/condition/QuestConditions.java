package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public final class QuestConditions {

    private static final Map<ResourceLocation, QuestConditionProviderType<?>> MAP = new HashMap<>();
    public static final QuestConditionProviderType<NoConditionProvider> NO_CONDITION_TYPE;

    public static <Q extends IQuestConditionProvider<?>> QuestConditionProviderType<Q> register(ResourceLocation location, IQuestConditionProviderSerializer<Q> serializer, BiFunction<QuestConditionProviderType<Q>, CompoundNBT, Q> fromNbtReader, boolean failsQuest, Trigger... triggers) {
        QuestConditionProviderType<Q> type = new QuestConditionProviderType<>(location, serializer, fromNbtReader, failsQuest, triggers);
        MAP.put(location, type);
        return type;
    }

    @SuppressWarnings("unchecked")
    public static <Q extends IQuestConditionProvider<?>> QuestConditionProviderType<Q> getByKey(ResourceLocation key) {
        return (QuestConditionProviderType<Q>) MAP.get(key);
    }

    public static <C extends IQuestCondition> CompoundNBT saveConditionToNbt(C condition) {
        CompoundNBT nbt = new CompoundNBT();
        IQuestConditionProvider<?> provider = condition.getProviderType();
        nbt.putString("type", provider.getType().getId().toString());
        provider.saveInternalData(nbt);
        condition.saveData(nbt);
        return nbt;
    }

    public static <C extends IQuestCondition, P extends IQuestConditionProvider<C>> C getConditionFromNbt(CompoundNBT nbt) {
        ResourceLocation questTypeId = new ResourceLocation(nbt.getString("type"));
        QuestConditionProviderType<P> type = getByKey(questTypeId);
        P provider = type.fromNbt(nbt);
        C condition = provider.makeConditionInstance();
        condition.loadData(nbt);
        return condition;
    }

    private static <Q extends IQuestConditionProvider<?>> QuestConditionProviderType<Q> register(String id, IQuestConditionProviderSerializer<Q> serializer, BiFunction<QuestConditionProviderType<Q>, CompoundNBT, Q> fromNbtReader, boolean failsQuest, Trigger... triggers) {
        return register(GunsRPG.makeResource(id), serializer, fromNbtReader, failsQuest, triggers);
    }

    static {
        NO_CONDITION_TYPE = register("empty", SimpleProviderSerializer.withConstantResult(() -> NoConditionProvider.NO_CONDITION), (type, nbt) -> NoConditionProvider.NO_CONDITION, false);
        register("used_item", new SpecificWeaponConditionProvider.Serializer(), SpecificWeaponConditionProvider::fromNbt, false, Trigger.ENTITY_KILLED);
        register("debuff", new ActiveDebuffConditionProvider.Serializer(), ActiveDebuffConditionProvider::fromNbt, false, Trigger.TICK);
        register("no_food", SimpleProviderSerializer.withResultOf(NoFoodConditionProvider::new), NoFoodConditionProvider::fromNbt, true, Trigger.TICK);
        register("no_heal", SimpleProviderSerializer.withResultOf(NoHealConditionProvider::new), NoHealConditionProvider::fromNbt, true, Trigger.TICK);
        register("explode", SimpleProviderSerializer.withResultOf(ExplodeConditionProvider::new), ExplodeConditionProvider::fromNbt, false, Trigger.ENTITY_KILLED);
        register("equipment", new EquipmentConditionProvider.Serializer(), EquipmentConditionProvider::fromNbt, false, Trigger.TICK);
        register("aggro", new HasAggroConditionProvider.Serializer(), HasAggroConditionProvider::fromNbt, false, Trigger.ENTITY_KILLED);
        register("distance", new DistanceConditionProvider.Serializer(), DistanceConditionProvider::fromNbt, false, Trigger.ENTITY_KILLED);
        register("no_damage_taken", SimpleProviderSerializer.withResultOf(NoDamageTakenConditionProvider::new), NoDamageTakenConditionProvider::fromNbt, true, Trigger.DAMAGE_TAKEN);
        register("no_damage_given", SimpleProviderSerializer.withResultOf(NoDamageGivenConditionProvider::new), NoDamageGivenConditionProvider::fromNbt, true, Trigger.DAMAGE_GIVEN);
        register("is_headshot", new HeadshotConditionProvider.Serializer(), HeadshotConditionProvider::fromNbt, false, Trigger.ENTITY_KILLED);
        register("unique", SimpleProviderSerializer.withResultOf(UniqueMobKillsConditionProvider::new), UniqueMobKillsConditionProvider::fromNbt, false, Trigger.ENTITY_KILLED);
    }
}
