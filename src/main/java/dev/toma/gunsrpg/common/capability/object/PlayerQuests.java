package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.IQuests;
import dev.toma.gunsrpg.common.quests.IQuest;
import dev.toma.gunsrpg.common.quests.IQuestInstance;

import java.util.IdentityHashMap;
import java.util.Map;

public class PlayerQuests implements IQuests {

    private final Map<IQuest, IQuestInstance> typeMap = new IdentityHashMap<>();

    @Override
    public boolean hasQuest(IQuest quest) {
        return getByType(quest) != null;
    }

    @Override
    public IQuestInstance getByType(IQuest quest) {
        return typeMap.get(quest);
    }
}
