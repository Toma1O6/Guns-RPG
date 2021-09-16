package dev.toma.gunsrpg.common.quests;

import dev.toma.gunsrpg.common.quests.tracking.IProgressionTracker;

public interface IQuestInstance {

    IProgressionTracker<?> getTracker();

    IQuest behaviour();
}
