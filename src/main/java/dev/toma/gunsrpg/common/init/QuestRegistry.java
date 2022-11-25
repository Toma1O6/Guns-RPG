package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quest.reward.*;
import dev.toma.questing.init.QuestingRegistries;
import dev.toma.questing.init.SimpleRegistry;
import dev.toma.questing.reward.AbstractItemReward;
import dev.toma.questing.reward.RewardTransformerType;
import dev.toma.questing.reward.RewardType;
import dev.toma.questing.utils.IdentifierHolder;
import net.minecraft.util.ResourceLocation;

public final class QuestRegistry {

    public static final RewardType<WeightedReward> WEIGHTED_REWARD = new RewardType<>(new WeightedReward.Serializer());
    public static final RewardType<TieredReward> TIERED_REWARD = new RewardType<>(data -> TieredReward.INSTANCE);
    public static final RewardType<PointReward> POINT_REWARD = new RewardType<>(new PointReward.Serializer());

    public static final RewardTransformerType<Integer, CountByAttributeTransformer> COUNT_BY_ATTRIBUTE_TRANSFORMER = new RewardTransformerType<>(internalId("output_modifier"), new CountByAttributeTransformer.Serializer(), Integer.class);
    public static final RewardTransformerType<AbstractItemReward.ItemList, SetCrystalTransformer> SET_CRYSTAL_TRANSFORMER = new RewardTransformerType<>(internalId("crystal"), new SetCrystalTransformer.Serializer(), AbstractItemReward.ItemList.class);

    public static void register() {
        register(QuestingRegistries.REWARDS, internalId("weighted"), WEIGHTED_REWARD);
        register(QuestingRegistries.REWARDS, internalId("tiered"), TIERED_REWARD);
        register(QuestingRegistries.REWARDS, internalId("point"), POINT_REWARD);

        register(QuestingRegistries.REWARD_TRANSFORMERS, COUNT_BY_ATTRIBUTE_TRANSFORMER);
        register(QuestingRegistries.REWARD_TRANSFORMERS, SET_CRYSTAL_TRANSFORMER);
    }

    private static <K, V> void register(SimpleRegistry<K, V> registry, K key, V value) {
        registry.register(key, value);
    }

    private static <V extends IdentifierHolder> void register(SimpleRegistry<ResourceLocation, V> registry, V value) {
        register(registry, value.getIdentifier(), value);
    }

    private static ResourceLocation internalId(String id) {
        return GunsRPG.makeResource(id);
    }
}
