package dev.toma.gunsrpg.integration.questing.reward.instance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.integration.questing.reward.provider.WeightedRewardProvider;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.questing.common.component.reward.RewardType;
import dev.toma.questing.common.component.reward.instance.EmptyReward;
import dev.toma.questing.common.component.reward.instance.Reward;
import dev.toma.questing.common.component.reward.instance.RewardHolder;
import dev.toma.questing.common.component.reward.provider.RewardProvider;
import dev.toma.questing.common.quest.instance.Quest;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collections;
import java.util.List;

public class WeightedReward implements Reward, RewardHolder {

    public static final Codec<WeightedReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WeightedRewardProvider.CODEC.fieldOf("provider").forGetter(t -> t.provider),
            RewardType.REWARD_CODEC.fieldOf("reward").forGetter(t -> t.reward)
    ).apply(instance, WeightedReward::new));
    private final WeightedRewardProvider provider;
    private final Reward reward;

    public WeightedReward(WeightedRewardProvider provider, PlayerEntity player, Quest quest) {
        this.provider = provider;
        WeightedRandom.Entry<RewardProvider<?>> entry = this.provider.getEntries().getRandom();
        this.reward = entry != null ? entry.get().createReward(player, quest) : EmptyReward.EMPTY;
    }

    public WeightedReward(WeightedRewardProvider provider, Reward reward) {
        this.provider = provider;
        this.reward = reward;
    }

    @Override
    public void award(PlayerEntity playerEntity, Quest quest) {
        this.reward.award(playerEntity, quest);
    }

    @Override
    public List<Reward> getRewards() {
        return Collections.singletonList(reward);
    }

    @Override
    public WeightedRewardProvider getProvider() {
        return provider;
    }
}
