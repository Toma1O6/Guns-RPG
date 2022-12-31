package dev.toma.gunsrpg.integration.questing.reward.instance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.integration.questing.TieredQuest;
import dev.toma.gunsrpg.integration.questing.loader.QuestRewardLoader;
import dev.toma.gunsrpg.integration.questing.reward.provider.TieredRewardProvider;
import dev.toma.questing.common.component.reward.RewardType;
import dev.toma.questing.common.component.reward.instance.Reward;
import dev.toma.questing.common.component.reward.instance.RewardHolder;
import dev.toma.questing.common.component.reward.provider.RewardProvider;
import dev.toma.questing.common.quest.Quest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.List;

public class TieredReward implements Reward, RewardHolder {

    public static final Codec<TieredReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TieredRewardProvider.CODEC.fieldOf("provider").forGetter(t -> t.provider),
            RewardType.REWARD_CODEC.fieldOf("reward").forGetter(t -> t.reward)
    ).apply(instance, TieredReward::new));
    private final TieredRewardProvider provider;
    private final Reward reward;

    public TieredReward(TieredRewardProvider provider, PlayerEntity player, Quest quest) {
        this.provider = provider;
        if (quest instanceof TieredQuest) {
            int questTier = ((TieredQuest) quest).getQuestTier();
            RewardProvider<?> rewardProvider = getTieredReward(questTier);
            this.reward = rewardProvider.createReward(player, quest);
        } else {
            this.reward = null; // TODO empty reward
        }
    }

    public TieredReward(TieredRewardProvider provider, Reward reward) {
        this.provider = provider;
        this.reward = reward;
    }

    @Override
    public void award(PlayerEntity player, Quest quest) {
        this.reward.award(player, quest);
    }

    @Override
    public List<Reward> getRewards() {
        return Collections.singletonList(this.reward);
    }

    @Override
    public TieredRewardProvider getProvider() {
        return provider;
    }

    public static RewardProvider<?> getTieredReward(int tier) {
        RewardTier rewardTier = RewardTier.getTier(tier);
        QuestRewardLoader rewardLoader = GunsRPG.getModLifecycle().getQuestManager().rewardLoader;
        return rewardLoader.getTieredReward(rewardTier);
    }

    public enum RewardTier {

        T1, T2, T3, T4, T5, T6, T7, T8;

        private final ResourceLocation identifier;

        RewardTier() {
            this.identifier = GunsRPG.makeResource("tier_" + (this.ordinal() + 1));
        }

        public ResourceLocation getIdentifier() {
            return identifier;
        }

        public static RewardTier getTier(int tier) {
            int index = MathHelper.clamp(tier, 0, 7);
            return values()[index];
        }
    }
}
