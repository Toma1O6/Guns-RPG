package dev.toma.gunsrpg.integration.questing.quest;

import dev.toma.gunsrpg.integration.questing.Tiered;
import dev.toma.questing.common.quest.instance.AbstractQuest;

public class TieredQuest extends AbstractQuest implements Tiered {

    private final TieredQuestProvider provider;
    private final int tier;

    public TieredQuest(TieredQuestProvider provider, QuestData questData, int tier) {
        super(questData);
        this.provider = provider;
        this.tier = tier;
    }

    public TieredQuest(TieredQuestProvider provider) {
        this.provider = provider;
        this.tier = 0;
    }

    @Override
    public TieredQuestProvider getProvider() {
        return provider;
    }

    @Override
    public int getTier() {
        return tier;
    }
}
