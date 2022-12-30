package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.integration.questing.area.ConfigurableAreaProvider;
import dev.toma.gunsrpg.integration.questing.area.TargettedEntitySpawner;
import dev.toma.gunsrpg.integration.questing.area.WeightedSpawner;
import dev.toma.gunsrpg.integration.questing.condition.DebuffCondition;
import dev.toma.gunsrpg.integration.questing.condition.HeadshotCondition;
import dev.toma.gunsrpg.integration.questing.condition.UniqueMobKillsCondition;
import dev.toma.gunsrpg.integration.questing.reward.*;
import dev.toma.questing.common.area.AreaType;
import dev.toma.questing.common.area.spawner.SpawnerType;
import dev.toma.questing.common.condition.ConditionType;
import dev.toma.questing.common.init.QuestingRegistries;
import dev.toma.questing.common.reward.AbstractItemReward;
import dev.toma.questing.common.reward.RewardTransformerType;
import dev.toma.questing.common.reward.RewardType;
import net.minecraft.util.ResourceLocation;

public final class QuestRegistry {

    public static final RewardType<WeightedReward> WEIGHTED_REWARD = new RewardType<>(internalId("weighted"), WeightedReward.CODEC);
    public static final RewardType<TieredReward> TIERED_REWARD = new RewardType<>(internalId("tiered"), TieredReward.codec());
    public static final RewardType<PointReward> POINT_REWARD = new RewardType<>(internalId("point"), PointReward.CODEC);
    public static final RewardType<GunsrpgChoiceReward> CHOICE_REWARD = new RewardType<>(internalId("choice"), GunsrpgChoiceReward.CODEC);

    public static final RewardTransformerType<Integer, CountByAttributeTransformer> COUNT_BY_ATTRIBUTE_TRANSFORMER = new RewardTransformerType<>(internalId("output_modifier"), CountByAttributeTransformer.CODEC, Integer.class);
    public static final RewardTransformerType<AbstractItemReward.ItemList, SetCrystalTransformer> SET_CRYSTAL_TRANSFORMER = new RewardTransformerType<>(internalId("crystal"), SetCrystalTransformer.CODEC, AbstractItemReward.ItemList.class);

    public static final SpawnerType<WeightedSpawner> WEIGHTED_SPAWNER = new SpawnerType<>(internalId("weighted"), WeightedSpawner.CODEC);
    public static final SpawnerType<TargettedEntitySpawner> ENTITY_SPAWNER = new SpawnerType<>(internalId("entity"), TargettedEntitySpawner.CODEC);

    public static final AreaType<ConfigurableAreaProvider> CONFIGURED_AREA = new AreaType<>(internalId("configured"), ConfigurableAreaProvider.CODEC);

    public static final ConditionType<UniqueMobKillsCondition> UNIQUE_MOBS_CONDITION = new ConditionType<>(internalId("unique_mobs"), UniqueMobKillsCondition.CODEC);
    public static final ConditionType<DebuffCondition> DEBUFF_CONDITION = new ConditionType<>(internalId("debuff"), DebuffCondition.CODEC);
    public static final ConditionType<HeadshotCondition> HEADSHOT_CONDITION = new ConditionType<>(internalId("headshot"), HeadshotCondition.CODEC);

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
    }

    private static ResourceLocation internalId(String id) {
        return GunsRPG.makeResource(id);
    }
}
