package dev.toma.gunsrpg.common.quests.reward;

public final class QuestRewardList {

    public static final QuestRewardList EMPTY_LIST = new QuestRewardList(new IQuestItemProvider[0]);
    private final IQuestItemProvider[] itemProviders;

    public QuestRewardList(IQuestItemProvider[] itemProviders) {
        this.itemProviders = itemProviders;
    }

    public IQuestItemProvider[] getItemProviders() {
        return itemProviders;
    }
}
