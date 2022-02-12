package dev.toma.gunsrpg.resource.util.functions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JSONUtils;

public class ComparatorFunction implements IFunction {

    public static final IInputComparator EACH = (input, value) -> input % value == 0;
    public static final IInputComparator EQUAL = (input, value) -> input == value;
    public static final IInputComparator SMALLER = (input, value) -> input < value;
    public static final IInputComparator SMALLER_EQUAL = (input, value) -> input <= value;
    public static final IInputComparator BIGGER = (input, value) -> input > value;
    public static final IInputComparator BIGGER_EQUAL = (input, value) -> input >= value;

    private final IInputComparator inputComparator;
    private final int value;

    public ComparatorFunction(IInputComparator inputComparator, int value) {
        this.inputComparator = inputComparator;
        this.value = value;
    }

    @Override
    public boolean canApplyFor(int input) {
        return inputComparator.isMatch(input, value);
    }

    @FunctionalInterface
    public interface IInputComparator {
        boolean isMatch(int input, int value);
    }

    public static final class Adapter implements IFunctionAdapter<ComparatorFunction> {

        private final IInputComparator inputComparator;

        public Adapter(IInputComparator inputComparator) {
            this.inputComparator = inputComparator;
        }

        @Override
        public ComparatorFunction resolveFromJson(JsonObject object) throws JsonParseException {
            int value = JSONUtils.getAsInt(object, "value");
            return new ComparatorFunction(inputComparator, value);
        }
    }
}
