package dev.toma.gunsrpg.common.quests.quest;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.TieredCondition;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardList;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardManager;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerListener;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class Quest {

    private final Multimap<Trigger, ITriggerListener> triggerListeners = ArrayListMultimap.create();
    private final List<IQuestCondition> conditions = new ArrayList<>();
    private final IQuestData questData;
    private final int rewardTier;
    private UUID assignedById;

    public Quest(QuestProperties properties) {
        questData = properties.data.copy();
        questData.registerTriggers(triggerListeners::put);
        int tier = properties.rewardTier;
        for (IQuestConditionProvider provider : properties.conditionProviders) {
            conditions.add(provider.getCondition());
        }
        for (TieredCondition tieredCondition : properties.tieredConditions) {
            IQuestCondition condition = tieredCondition.getCondition();
            // TODO not empty check
            if (true) {
                conditions.add(condition);
                tier += tieredCondition.getTierModifier();
            }
        }
        rewardTier = tier;
    }

    public QuestReward generateReward(QuestReward.Options options) {
        QuestRewardManager manager = GunsRPG.getModLifecycle().quests().getRewardManager();
        QuestRewardList list = manager.getTieredRewards(rewardTier);
        return QuestReward.generate(list, options);
    }

    public void onAssigned(PlayerEntity player, IPlayerData data, UUID assignedById) {
        this.assignedById = assignedById;
    }

    public void failQuest(PlayerEntity player) {}

    public void trigger(Trigger trigger, IPropertyReader reader) {
        Collection<ITriggerListener> listeners = triggerListeners.get(trigger);
        if (listeners != null) {
            boolean failedChecks = false;
            for (ITriggerListener listener : listeners) {
                TriggerResponseStatus status = listener.handleTriggerEvent(trigger, reader);
                if (status == TriggerResponseStatus.FAIL) {
                    failedChecks = true;
                    break;
                }
            }
            if (!failedChecks) {
                questData.handleProgress(trigger, reader);
            }
        }
    }

    public interface ITriggerRegistration {
        void addEntry(Trigger trigger, ITriggerListener listener);
    }
}
