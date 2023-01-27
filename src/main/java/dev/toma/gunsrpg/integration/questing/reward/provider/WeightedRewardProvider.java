package dev.toma.gunsrpg.integration.questing.reward.provider;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.integration.questing.reward.instance.WeightedReward;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.questing.common.component.reward.RewardType;
import dev.toma.questing.common.component.reward.provider.RewardProvider;
import dev.toma.questing.common.quest.instance.Quest;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeightedRewardProvider implements RewardProvider<WeightedReward> {

    public static final Codec<WeightedRewardProvider> CODEC = WeightedRandom.Entry.codec(RewardType.PROVIDER_CODEC)
            .listOf()
            .xmap(WeightedRewardProvider::new, t -> new ArrayList<>(Arrays.asList(t.entries.getValues())))
            .fieldOf("values").codec();
    private final WeightedRandom<WeightedRandom.Entry<RewardProvider<?>>> entries;

    public WeightedRewardProvider(List<WeightedRandom.Entry<RewardProvider<?>>> entries) {
        this.entries = WeightedRandom.fromEntries(entries);
    }

    @Override
    public WeightedReward createReward(PlayerEntity playerEntity, Quest quest) {
        return new WeightedReward(this, playerEntity, quest);
    }

    @Override
    public RewardType<WeightedReward, ?> getType() {
        return QuestRegistry.WEIGHTED_REWARD;
    }

    public WeightedRandom<WeightedRandom.Entry<RewardProvider<?>>> getEntries() {
        return entries;
    }
}
