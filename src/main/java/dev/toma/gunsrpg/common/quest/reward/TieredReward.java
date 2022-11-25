package dev.toma.gunsrpg.common.quest.reward;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quest.TieredQuest;
import dev.toma.gunsrpg.common.quest.loader.QuestRewardLoader;
import dev.toma.questing.quest.Quest;
import dev.toma.questing.reward.IReward;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public enum TieredReward implements IReward {

    INSTANCE;

    @Override
    public void awardPlayer(PlayerEntity player, Quest quest) {
        if (quest instanceof TieredQuest) {
            int questTier = ((TieredQuest) quest).getQuestTier();
            RewardTier rewardTier = RewardTier.getTier(questTier);
            QuestRewardLoader rewardLoader = GunsRPG.getModLifecycle().getQuestManager().rewardLoader;
            IReward reward = rewardLoader.getTieredReward(rewardTier);
            reward.awardPlayer(player, quest);
        }
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
