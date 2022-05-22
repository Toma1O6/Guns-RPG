package dev.toma.gunsrpg.common.quests.quest;

public enum QuestStatus {

    CREATED(),
    ACTIVE(),
    COMPLETED(true),
    FAILED(),
    CLAIMED();

    private final boolean showRewards;

    QuestStatus(boolean showRewards) {
        this.showRewards = showRewards;
    }

    QuestStatus() {
        this(false);
    }

    public boolean shouldShowRewards() {
        return showRewards;
    }
}
