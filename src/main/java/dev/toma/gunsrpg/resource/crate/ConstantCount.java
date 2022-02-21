package dev.toma.gunsrpg.resource.crate;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
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
        public ICountFunction deserialize(JsonObject data) {
            int value = JSONUtils.getAsInt(data, "value", 1);
            if (value <= 0 || value > 64) {
                throw new JsonSyntaxException("Value is out of bounds. Make sure it's defined in <1; 64> interval");
            }
            return constant(value);
        }
    }
}
