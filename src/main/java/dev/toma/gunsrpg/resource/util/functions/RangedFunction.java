package dev.toma.gunsrpg.resource.util.functions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JSONUtils;

public class RangedFunction implements IFunction {

    public static final IRangeComparator BETWEEN_INCLUSIVE = (input, min, max) -> input >= min && input <= max;
    public static final IRangeComparator BETWEEN_EXCLUSIVE = (input, min, max) -> input > min && input < max;

    private final IRangeComparator rangeComparator;
    private final int min;
    private final int max;

    public RangedFunction(IRangeComparator rangeComparator, int min, int max) {
        this.rangeComparator = rangeComparator;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean canApplyFor(int input) {
        return rangeComparator.isWithinRange(input, min, max);
    }

    @FunctionalInterface
    public interface IRangeComparator {
        boolean isWithinRange(int input, int min, int max);
    }

    public static final class Adapter implements IFunctionAdapter<RangedFunction> {

        private final IRangeComparator rangeComparator;

        public Adapter(IRangeComparator rangeComparator) {
            this.rangeComparator = rangeComparator;
        }

        @Override
        public RangedFunction resolveFromJson(JsonObject object) throws JsonParseException {
            int min = JSONUtils.getAsInt(object, "min");
            int max = JSONUtils.getAsInt(object, "max");
            if (min > max) {
                throw new JsonSyntaxException("'max' value cannot be smaller than 'min' value");
            }
            return new RangedFunction(rangeComparator, min, max);
        }
    }
}
