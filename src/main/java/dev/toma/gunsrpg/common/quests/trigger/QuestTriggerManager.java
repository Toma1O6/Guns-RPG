package dev.toma.gunsrpg.common.quests.trigger;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.IQuest;
import dev.toma.gunsrpg.common.quests.IQuestInstance;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.reward.Reward;
import dev.toma.gunsrpg.common.quests.reward.RewardManager;
import dev.toma.gunsrpg.common.quests.tracking.IProgressionTracker;
import net.minecraft.entity.player.PlayerEntity;

import java.util.*;

public class QuestTriggerManager implements IQuestTriggerManager {

    private Map<TriggerType<?>, List<IQuestInstance>> activeTriggers;
    private Map<UUID, List<IQuestInstance>> playerQuests;

    @SuppressWarnings("unchecked")
    @Override
    public <CTX> void doTrigger(TriggerType<CTX> type, CTX context, PlayerEntity player) {
        List<IQuestInstance> quests = activeTriggers.getOrDefault(type, Collections.emptyList());
        for (IQuestInstance quest : quests) {
            IProgressionTracker<CTX> tracker = (IProgressionTracker<CTX>) quest.getTracker();
            IQuest behaviour = quest.behaviour();
            Set<IQuestCondition> conditions = behaviour.getConditions();
            int tierAdd = 0;
            for (IQuestCondition condition : conditions) {
                tierAdd += condition.getRewardTierModifier();
                if (!condition.test(player)) {
                    return;
                }
            }
            tracker.advance(context);
            if (tracker.isComplete()) {
                RewardManager rewardManager = GunsRPG.getModLifecycle().quests().getRewards();
                int tier = Math.min(behaviour.getRewardTier() + tierAdd, 7);
                Reward reward = rewardManager.byTier(tier);
                reward.awardTo(player);
                questRemoved(player, quest);
            }
        }
    }

    @Override
    public void questAdded(PlayerEntity player, IQuestInstance quest) {
        // update map
        updateTickables();
    }

    @Override
    public void questRemoved(PlayerEntity player, IQuestInstance quest) {
        // update map
        updateTickables();
    }

    @Override
    public void playerAddedToServer(PlayerEntity player) {
        // populate map
        updateTickables();
    }

    @Override
    public void playerRemovedFromServer(PlayerEntity player) {
        // cleanup map
        updateTickables();
    }

    private List<?> createCleanList(TriggerType type) {
        return Collections.emptyList();
    }

    private void updateTickables() {

    }
}
