package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.quests.IQuest;
import dev.toma.gunsrpg.common.quests.IQuestInstance;

public interface IQuests {

    boolean hasQuest(IQuest quest);

    IQuestInstance getByType(IQuest quest);
}
