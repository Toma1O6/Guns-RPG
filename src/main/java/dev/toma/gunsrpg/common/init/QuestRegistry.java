package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.integration.questing.area.ConfigurableAreaProvider;
import dev.toma.gunsrpg.integration.questing.area.TargettedEntitySpawner;
import dev.toma.gunsrpg.integration.questing.area.WeightedSpawner;
import dev.toma.gunsrpg.integration.questing.reward.*;
import dev.toma.questing.area.AreaType;
import dev.toma.questing.area.spawner.SpawnerType;
import dev.toma.questing.init.QuestingRegistries;
import dev.toma.questing.init.SimpleRegistry;
import dev.toma.questing.reward.AbstractItemReward;
import dev.toma.questing.reward.RewardTransformerType;
import dev.toma.questing.reward.RewardType;
import dev.toma.questing.utils.IdentifierHolder;
import net.minecraft.util.ResourceLocation;

public final class QuestRegistry {

    public static final RewardType<WeightedReward> WEIGHTED_REWARD = new RewardType<>(internalId("weighted"), new WeightedReward.Serializer());
    public static final RewardType<TieredReward> TIERED_REWARD = new RewardType<>(internalId("tiered"), data -> TieredReward.INSTANCE);
    public static final RewardType<PointReward> POINT_REWARD = new RewardType<>(internalId("point"), new PointReward.Serializer());

    public static final RewardTransformerType<Integer, CountByAttributeTransformer> COUNT_BY_ATTRIBUTE_TRANSFORMER = new RewardTransformerType<>(internalId("output_modifier"), new CountByAttributeTransformer.Serializer(), Integer.class);
    public static final RewardTransformerType<AbstractItemReward.ItemList, SetCrystalTransformer> SET_CRYSTAL_TRANSFORMER = new RewardTransformerType<>(internalId("crystal"), new SetCrystalTransformer.Serializer(), AbstractItemReward.ItemList.class);

    public static final SpawnerType<WeightedSpawner> WEIGHTED_SPAWNER = new SpawnerType<>(internalId("weighted"), new WeightedSpawner.Serializer());
    public static final SpawnerType<TargettedEntitySpawner> ENTITY_SPAWNER = new SpawnerType<>(internalId("entity"), new TargettedEntitySpawner.Serializer());

    public static final AreaType<ConfigurableAreaProvider> CONFIGURED_AREA = new AreaType<>(internalId("configured"), new ConfigurableAreaProvider.Serializer());

    public static void register() {
        register(QuestingRegistries.REWARDS, WEIGHTED_REWARD);
        register(QuestingRegistries.REWARDS, TIERED_REWARD);
        register(QuestingRegistries.REWARDS, POINT_REWARD);

        register(QuestingRegistries.REWARD_TRANSFORMERS, COUNT_BY_ATTRIBUTE_TRANSFORMER);
        register(QuestingRegistries.REWARD_TRANSFORMERS, SET_CRYSTAL_TRANSFORMER);

        register(QuestingRegistries.SPAWNER, WEIGHTED_SPAWNER);
        register(QuestingRegistries.SPAWNER, ENTITY_SPAWNER);

        register(QuestingRegistries.AREA, CONFIGURED_AREA);
    }

    private static <V extends IdentifierHolder> void register(SimpleRegistry<ResourceLocation, V> registry, V value) {
        ResourceLocation key = value.getIdentifier();
        registry.register(key, value);
    }

    private static ResourceLocation internalId(String id) {
        return GunsRPG.makeResource(id);
    }
}
