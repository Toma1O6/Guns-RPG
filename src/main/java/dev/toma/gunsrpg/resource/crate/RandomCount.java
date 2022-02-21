package dev.toma.gunsrpg.resource.crate;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.util.JSONUtils;

import java.util.Random;

public class RandomCount implements ICountFunction {

    private static final Random RANDOM = new Random();

    private final int lowerBound;
    private final int upperBound;

    private RandomCount(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static ICountFunction fromInterval(int lower, int upper) {
        return new RandomCount(lower, upper);
    }

    @Override
    public int getCount() {
        return lowerBound + RANDOM.nextInt(upperBound + 1);
    }

    public static class Adapter implements ICountFunctionAdapter {

        @Override
        public ICountFunction deserialize(JsonObject data) {
            int lower = JSONUtils.getAsInt(data, "min");
            int upper = JSONUtils.getAsInt(data, "max");
            if (lower == upper) {
                GunsRPG.log.warn(LootManager.MARKER, "Using 'rng' function with constant range, maybe you wanted to use 'const' function instead");
            }
            if (lower > upper) {
                throw new JsonSyntaxException("Lower bound cannot be bigger than upper bound!");
            }
            validateInRange(lower);
            validateInRange(upper);
            return fromInterval(lower, upper);
        }

        private void validateInRange(int value) {
            if (value <= 0 || value > 64) {
                throw new JsonSyntaxException("Value is out of bounds. Make sure it's defined in <1; 64> interval");
            }
        }
    }
}
