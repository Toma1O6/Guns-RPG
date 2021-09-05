package dev.toma.gunsrpg.common.quests.reward;

public final class TierRange {

    private final int min; // inclusive
    private final int max; // exclusive

    private TierRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static TierRange range(int minInclusive, int maxExclusive) {
        return new TierRange(minInclusive, maxExclusive);
    }

    public boolean isValid(int value) {
        return value >= min && value < max;
    }
}
