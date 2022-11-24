package dev.toma.gunsrpg.common.quest.reward;

import dev.toma.gunsrpg.common.quest.TieredQuest;
import dev.toma.questing.quest.Quest;
import dev.toma.questing.reward.IReward;
import net.minecraft.entity.player.PlayerEntity;

public enum TieredReward implements IReward {

    INSTANCE;

    @Override
    public void awardPlayer(PlayerEntity player, Quest quest) {
        // TODO load reward based on quest tier from QuestDataManager
        if (quest instanceof TieredQuest) {
            int questTier = ((TieredQuest) quest).getQuestTier();
        }
    }
}
