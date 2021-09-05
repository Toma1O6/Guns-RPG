package dev.toma.gunsrpg.common.quests.reward;

import java.util.List;

public final class QuestAwardList {

    private final TierRange range;
    private final List<Award> awardList;
    private final String category;

    public QuestAwardList(TierRange range, List<Award> awardList, String category) {
        this.range = range;
        this.awardList = awardList;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public boolean withinRange(int value) {
        return range.isValid(value);
    }
}
