package dev.toma.gunsrpg.common.quests.reward;

import dev.toma.gunsrpg.util.math.WeightedRandom;

public final class QuestRewardList {

    public static final QuestRewardList EMPTY_LIST = new QuestRewardList(new IQuestItemProvider[0]);
    private final WeightedRandom<IQuestItemProvider> itemProviders;

    public QuestRewardList(IQuestItemProvider[] itemProviders) {
        this.itemProviders = new WeightedRandom<>(IQuestItemProvider::getWeight, itemProviders);
    }

    public IQuestItemProvider[] listProviders() {
        return itemProviders.getValues();
    }

    public IQuestItemProvider getRandomProvider() {
        return itemProviders.getRandom();
    }

    public int size() {
        return itemProviders.getValueCount();
    }
}
