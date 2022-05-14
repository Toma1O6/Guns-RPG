package dev.toma.gunsrpg.common.quests.quest;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerListener;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.properties.IPropertyReader;

import java.util.Collection;
import java.util.UUID;

public class Quest<D extends IQuestData> implements IQuest<D> {

    private final Multimap<Trigger, ITriggerListener> triggerListeners = ArrayListMultimap.create();
    private final QuestScheme<D> scheme;
    private final IQuestCondition[] conditions;
    private final D questData;
    private final QuestReward reward;
    private final UUID uuid;

    private boolean active;

    public Quest(QuestScheme<D> scheme, IAttributeProvider provider, float loyaltyValue, UUID traderId) {
        this.scheme = scheme;
        this.uuid = traderId;
        this.questData = scheme.getData().copy();
        this.reward = null;
        this.conditions = null; // TODO
    }

    @Override
    public UUID getQuestInitiator() {
        return uuid;
    }

    @Override
    public D getActiveData() {
        return questData;
    }

    @Override
    public void onAssigned() {
        active = true;
    }

    @Override
    public void onCompleted() {
        active = false;
    }

    @Override
    public void onFailed() {
        active = false;
    }

    @Override
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
