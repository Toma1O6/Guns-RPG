package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.util.properties.IPropertyReader;

import java.util.UUID;

public interface IQuest<D> {

    UUID getQuestInitiator();

    D getActiveData();

    void onAssigned();

    void onCompleted();

    void onFailed();

    void trigger(Trigger trigger, IPropertyReader reader);
}
