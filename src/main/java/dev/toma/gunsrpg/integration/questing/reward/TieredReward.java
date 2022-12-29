package dev.toma.gunsrpg.integration.questing.reward;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.integration.questing.TieredQuest;
import dev.toma.gunsrpg.integration.questing.loader.QuestRewardLoader;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.common.reward.Reward;
import dev.toma.questing.common.reward.RewardType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public enum TieredReward implements Reward {

    INSTANCE;

    @Override
    public void awardPlayer(PlayerEntity player, Quest quest) {
        if (quest instanceof TieredQuest) {
            int questTier = ((TieredQuest) quest).getQuestTier();
            Reward reward = getTieredReward(questTier);
            reward.awardPlayer(player, quest);
        }
    }

    @Override
    public void generate(PlayerEntity playerEntity, Quest quest) {
    }

    @Override
    public Reward copy() {
        return INSTANCE;
    }

    @Override
    public RewardType<?> getType() {
        return QuestRegistry.TIERED_REWARD;
    }

    public static Reward getTieredReward(int tier) {
        RewardTier rewardTier = RewardTier.getTier(tier);
        QuestRewardLoader rewardLoader = GunsRPG.getModLifecycle().getQuestManager().rewardLoader;
        return rewardLoader.getTieredReward(rewardTier);
    }

    public static Codec<TieredReward> codec() {
        return Codec.unit(INSTANCE);
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
