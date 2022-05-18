package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public final class QuestConditions {

    private static final Map<ResourceLocation, QuestConditionProviderType<?>> MAP = new HashMap<>();
    public static final QuestConditionProviderType<NoConditionProvider> NO_CONDITION_TYPE;

    public static <Q extends IQuestConditionProvider<?>> QuestConditionProviderType<Q> register(ResourceLocation location, IQuestConditionProviderSerializer<Q> serializer, BiFunction<QuestConditionProviderType<Q>, CompoundNBT, Q> fromNbtReader) {
        QuestConditionProviderType<Q> type = new QuestConditionProviderType<>(location, serializer, fromNbtReader, false);
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

    private static <Q extends IQuestConditionProvider<?>> QuestConditionProviderType<Q> register(String id, IQuestConditionProviderSerializer<Q> serializer, BiFunction<QuestConditionProviderType<Q>, CompoundNBT, Q> fromNbtReader) {
        return register(GunsRPG.makeResource(id), serializer, fromNbtReader);
    }

    static {
        NO_CONDITION_TYPE = register("empty", SimpleProviderSerializer.withConstantResult(() -> NoConditionProvider.NO_CONDITION), (type, nbt) -> NoConditionProvider.NO_CONDITION);
        register("used_item", new SpecificWeaponConditionProvider.Serializer(), SpecificWeaponConditionProvider::fromNbt);
        register("debuff", new ActiveDebuffConditionProvider.Serializer(), ActiveDebuffConditionProvider::fromNbt);
        register("no_food", SimpleProviderSerializer.withResultOf(NoFoodConditionProvider::new), NoFoodConditionProvider::fromNbt);
        register("no_heal", SimpleProviderSerializer.withResultOf(NoHealConditionProvider::new), NoHealConditionProvider::fromNbt);
        register("explode", SimpleProviderSerializer.withResultOf(ExplodeConditionProvider::new), ExplodeConditionProvider::fromNbt);
        register("equipment", new EquipmentConditionProvider.Serializer(), EquipmentConditionProvider::fromNbt);
        register("aggro", new HasAggroConditionProvider.Serializer(), HasAggroConditionProvider::fromNbt);
        register("distance", new DistanceConditionProvider.Serializer(), DistanceConditionProvider::fromNbt);
        register("no_damage_taken", SimpleProviderSerializer.withResultOf(NoDamageTakenConditionProvider::new), NoDamageTakenConditionProvider::fromNbt);
        register("no_damage_given", SimpleProviderSerializer.withResultOf(NoDamageGivenConditionProvider::new), NoDamageGivenConditionProvider::fromNbt);
        register("is_headshot", new HeadshotConditionProvider.Serializer(), HeadshotConditionProvider::fromNbt);
        register("unique", SimpleProviderSerializer.withResultOf(UniqueMobKillsConditionProvider::new), UniqueMobKillsConditionProvider::fromNbt);
    }
}
