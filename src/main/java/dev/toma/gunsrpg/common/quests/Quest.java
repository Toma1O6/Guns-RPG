package dev.toma.gunsrpg.common.quests;

import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class Quest implements IQuest {

    private final ResourceLocation questID;
    private final Set<IQuestCondition> conditions;

    public Quest(ResourceLocation questID, Set<IQuestCondition> conditions) {
        this.questID = questID;
        this.conditions = conditions;
    }

    @Override
    public ResourceLocation getID() {
        return questID;
    }

    @Override
    public Set<IQuestCondition> getConditions() {
        return conditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quest quest = (Quest) o;
        return questID.equals(quest.questID);
    }

    @Override
    public int hashCode() {
        return questID.hashCode();
    }
}
