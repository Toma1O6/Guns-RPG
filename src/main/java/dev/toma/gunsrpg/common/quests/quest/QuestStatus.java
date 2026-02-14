package dev.toma.gunsrpg.common.quests.quest;

public enum QuestStatus {

    CREATED(),
    ACTIVE(),
    COMPLETED(),
    FAILED(),
    CLAIMED(),
    PAUSED();

    public boolean isActiveOrCompleted() {
        return this == ACTIVE || this == COMPLETED;
    }
}
