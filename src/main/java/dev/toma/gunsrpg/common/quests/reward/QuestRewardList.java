package dev.toma.gunsrpg.common.quests.reward;

public final class QuestRewardList {

    private final IQuestItemProvider[] itemProviders;

    public QuestRewardList(IQuestItemProvider[] itemProviders) {
        this.itemProviders = itemProviders;
    }
}
