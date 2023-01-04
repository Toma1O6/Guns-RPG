package dev.toma.gunsrpg.integration.questing.reward.provider;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.integration.questing.reward.instance.GunsrpgChoiceReward;
import dev.toma.questing.common.component.reward.RewardType;
import dev.toma.questing.common.component.reward.provider.RewardProvider;
import dev.toma.questing.common.quest.instance.Quest;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class GunsrpgChoiceRewardProvider implements RewardProvider<GunsrpgChoiceReward> {

    public static final Codec<GunsrpgChoiceRewardProvider> CODEC = RewardType.PROVIDER_CODEC.listOf()
            .xmap(GunsrpgChoiceRewardProvider::new, t -> t.rewardList).fieldOf("choices").codec();

    private final List<RewardProvider<?>> rewardList;

    public GunsrpgChoiceRewardProvider(List<RewardProvider<?>> rewardList) {
        this.rewardList = rewardList;
    }

    @Override
    public GunsrpgChoiceReward createReward(PlayerEntity playerEntity, Quest quest) {
        return new GunsrpgChoiceReward(this, playerEntity, quest);
    }

    @Override
    public RewardType<GunsrpgChoiceReward, ?> getType() {
        return QuestRegistry.CHOICE_REWARD;
    }

    public List<RewardProvider<?>> getRewardList() {
        return rewardList;
    }
}
