package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.integration.questing.area.instance.ConfiguredArea;
import dev.toma.gunsrpg.integration.questing.area.provider.ConfiguredAreaProvider;
import dev.toma.gunsrpg.integration.questing.area.spawner.TargettedEntitySpawner;
import dev.toma.gunsrpg.integration.questing.area.spawner.WeightedSpawner;
import dev.toma.gunsrpg.integration.questing.condition.instance.DebuffCondition;
import dev.toma.gunsrpg.integration.questing.condition.instance.HeadshotCondition;
import dev.toma.gunsrpg.integration.questing.condition.instance.UniqueMobKillsCondition;
import dev.toma.gunsrpg.integration.questing.condition.provider.DebuffConditionProvider;
import dev.toma.gunsrpg.integration.questing.condition.provider.HeadshotConditionProvider;
import dev.toma.gunsrpg.integration.questing.condition.provider.UniqueMobKillsConditionProvider;
import dev.toma.gunsrpg.integration.questing.reward.instance.GunsrpgChoiceReward;
import dev.toma.gunsrpg.integration.questing.reward.instance.PointReward;
import dev.toma.gunsrpg.integration.questing.reward.instance.TieredReward;
import dev.toma.gunsrpg.integration.questing.reward.instance.WeightedReward;
import dev.toma.gunsrpg.integration.questing.reward.provider.GunsrpgChoiceRewardProvider;
import dev.toma.gunsrpg.integration.questing.reward.provider.PointRewardProvider;
import dev.toma.gunsrpg.integration.questing.reward.provider.TieredRewardProvider;
import dev.toma.gunsrpg.integration.questing.reward.provider.WeightedRewardProvider;
import dev.toma.gunsrpg.integration.questing.reward.transformer.CountByAttributeTransformer;
import dev.toma.gunsrpg.integration.questing.reward.transformer.SetCrystalTransformer;
import dev.toma.gunsrpg.integration.questing.select.WeightedSelector;
import dev.toma.gunsrpg.integration.questing.task.instance.HandoverItemsTask;
import dev.toma.gunsrpg.integration.questing.task.provider.HandoverItemsTaskProvider;
import dev.toma.questing.common.component.area.AreaType;
import dev.toma.questing.common.component.area.spawner.SpawnerType;
import dev.toma.questing.common.component.condition.ConditionType;
import dev.toma.questing.common.component.reward.RewardType;
import dev.toma.questing.common.component.reward.transformer.RewardTransformerType;
import dev.toma.questing.common.component.selector.SelectorType;
import dev.toma.questing.common.component.task.TaskType;
import dev.toma.questing.common.init.QuestingRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class QuestRegistry {

    public static final RewardType<WeightedReward, WeightedRewardProvider> WEIGHTED_REWARD = new RewardType<>(internalId("weighted"), WeightedRewardProvider.CODEC, WeightedReward.CODEC);
    public static final RewardType<TieredReward, TieredRewardProvider> TIERED_REWARD = new RewardType<>(internalId("tiered"), TieredRewardProvider.CODEC, TieredReward.CODEC);
    public static final RewardType<PointReward, PointRewardProvider> POINT_REWARD = new RewardType<>(internalId("point"), PointRewardProvider.CODEC, PointReward.CODEC);
    public static final RewardType<GunsrpgChoiceReward, GunsrpgChoiceRewardProvider> CHOICE_REWARD = new RewardType<>(internalId("choice"), GunsrpgChoiceRewardProvider.CODEC, GunsrpgChoiceReward.CODEC);

    public static final RewardTransformerType<Integer, CountByAttributeTransformer> COUNT_BY_ATTRIBUTE_TRANSFORMER = new RewardTransformerType<>(internalId("output_modifier"), CountByAttributeTransformer.CODEC, Integer.class);
    public static final RewardTransformerType<ItemStack, SetCrystalTransformer> SET_CRYSTAL_TRANSFORMER = new RewardTransformerType<>(internalId("crystal"), SetCrystalTransformer.CODEC, ItemStack.class);

    public static final SpawnerType<WeightedSpawner> WEIGHTED_SPAWNER = new SpawnerType<>(internalId("weighted"), WeightedSpawner.CODEC);
    public static final SpawnerType<TargettedEntitySpawner> ENTITY_SPAWNER = new SpawnerType<>(internalId("entity"), TargettedEntitySpawner.CODEC);

    public static final AreaType<ConfiguredArea, ConfiguredAreaProvider> CONFIGURED_AREA = new AreaType<>(internalId("configured"), ConfiguredAreaProvider.CODEC, ConfiguredArea.CODEC);

    public static final ConditionType<UniqueMobKillsCondition, UniqueMobKillsConditionProvider> UNIQUE_MOBS_CONDITION = new ConditionType<>(internalId("unique_mobs"), UniqueMobKillsConditionProvider.CODEC, UniqueMobKillsCondition.CODEC);
    public static final ConditionType<DebuffCondition, DebuffConditionProvider> DEBUFF_CONDITION = new ConditionType<>(internalId("debuff"), DebuffConditionProvider.CODEC, DebuffCondition.CODEC);
    public static final ConditionType<HeadshotCondition, HeadshotConditionProvider> HEADSHOT_CONDITION = new ConditionType<>(internalId("headshot"), HeadshotConditionProvider.CODEC, HeadshotCondition.CODEC);
    public static final ConditionType<TieredCondition, TieredConditionProvider> TIERED_CONDITION = new ConditionType<>(internalId("tiered"), TieredConditionProvider.CODEC, TieredCondition.CODEC);

    public static final TaskType<HandoverItemsTask, HandoverItemsTaskProvider> HANDOVER_ITEMS_TASK = new TaskType<>(internalId("handover_items"), HandoverItemsTaskProvider.CODEC, HandoverItemsTask.CODEC);

    public static final SelectorType<?, ?> WEIGHTED_SELECTOR = new SelectorType<>(internalId("weighted"), WeightedSelector::codec);

    public static void register() {
        QuestingRegistries.REWARDS.register(WEIGHTED_REWARD);
        QuestingRegistries.REWARDS.register(TIERED_REWARD);
        QuestingRegistries.REWARDS.register(POINT_REWARD);
        QuestingRegistries.REWARDS.register(CHOICE_REWARD);

        QuestingRegistries.REWARD_TRANSFORMERS.register(COUNT_BY_ATTRIBUTE_TRANSFORMER);
        QuestingRegistries.REWARD_TRANSFORMERS.register(SET_CRYSTAL_TRANSFORMER);

        QuestingRegistries.SPAWNER.register(WEIGHTED_SPAWNER);
        QuestingRegistries.SPAWNER.register(ENTITY_SPAWNER);

        QuestingRegistries.AREA.register(CONFIGURED_AREA);

        QuestingRegistries.CONDITION.register(UNIQUE_MOBS_CONDITION);
        QuestingRegistries.CONDITION.register(DEBUFF_CONDITION);
        QuestingRegistries.CONDITION.register(HEADSHOT_CONDITION);
        QuestingRegistries.CONDITION.register(TIERED_CONDITION);

        QuestingRegistries.TASK.register(HANDOVER_ITEMS_TASK);
    }

    private static ResourceLocation internalId(String id) {
        return GunsRPG.makeResource(id);
    }
}
