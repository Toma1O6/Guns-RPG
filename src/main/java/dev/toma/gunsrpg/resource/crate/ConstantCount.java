package dev.toma.gunsrpg.resource.crate;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.resource.util.functions.IFunction;
import net.minecraft.util.JSONUtils;

public class ConstantCount implements ICountFunction {

    private final int value;

    private ConstantCount(int value) {
        this.value = value;
    }

    public static ICountFunction constant(int value) {
        return new ConstantCount(value);
    }

    @Override
    public int getCount() {
        return value;
    }

    public static class Adapter implements ICountFunctionAdapter {

        @Override
        public ICountFunction deserialize(JsonObject data, IFunction range) {
            int value = JSONUtils.getAsInt(data, "value", 1);
            if (!range.canApplyFor(value)) {
                throw new JsonSyntaxException("Value is out of bounds.");
            }
            return constant(value);
        }
    }
}
