package dev.toma.gunsrpg.common.quests.reward;

import java.util.List;
import java.util.Random;

public final class QuestAwardList {

    private final TierRange range;
    private final List<Reward> rewardList;
    private final String category;

    public QuestAwardList(TierRange range, List<Reward> rewardList, String category) {
        this.range = range;
        this.rewardList = rewardList;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public boolean withinRange(int value) {
        return range.isValid(value);
    }

    public Reward getRandomReward(int tier, Random random) {
        int value = tier - range.min();
        if (value == 0) {
            return rewardList.get(0);
        } else if (value > 0) {
            return rewardList.get(random.nextInt(value));
        } else {
            throw new IllegalArgumentException();
        }
    }
}
