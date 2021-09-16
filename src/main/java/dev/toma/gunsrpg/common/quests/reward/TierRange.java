package dev.toma.gunsrpg.common.quests.reward;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JSONUtils;

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

    public static TierRange fromJson(JsonObject obj, int limit) throws JsonParseException {
        int min = JSONUtils.getAsInt(obj, "min", 0);
        int max = JSONUtils.getAsInt(obj, "max", limit);
        if (max < min)
            throw new JsonSyntaxException("Invalid range, max value cannot be smaller than min value. Got range [" + min + ";" + max + "]");
        return range(min, max);
    }

    public boolean isValid(int value) {
        return value >= min && value < max;
    }

    public int length() {
        return max - min;
    }

    public int min() {
        return min;
    }

    public int max() {
        return max;
    }
}
