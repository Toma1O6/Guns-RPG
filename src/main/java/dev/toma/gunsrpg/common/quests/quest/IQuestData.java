package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.util.properties.IPropertyReader;

public interface IQuestData {

    <Q extends IQuestData> Q copy();

    // TODO make abstract
    default void registerTriggers(Quest.ITriggerRegistration registration) {}

    // TODO make abstract
    default void handleProgress(Trigger cause, IPropertyReader reader) {}
}
