package dev.toma.gunsrpg.integration.questing.reward.provider;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.integration.questing.reward.instance.TieredReward;
import dev.toma.questing.common.component.reward.RewardType;
import dev.toma.questing.common.component.reward.provider.RewardProvider;
import dev.toma.questing.common.quest.Quest;
import net.minecraft.entity.player.PlayerEntity;

public class TieredRewardProvider implements RewardProvider<TieredReward> {

    public static final TieredRewardProvider INSTANCE = new TieredRewardProvider();
    public static final Codec<TieredRewardProvider> CODEC = Codec.unit(INSTANCE);

    @Override
    public RewardType<TieredReward, ?> getType() {
        return QuestRegistry.TIERED_REWARD;
    }

    @Override
    public TieredReward createReward(PlayerEntity playerEntity, Quest quest) {
        return new TieredReward(this, playerEntity, quest);
    }
}
