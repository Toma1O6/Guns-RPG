package dev.toma.gunsrpg.resource.util.functions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JSONUtils;

import java.util.Arrays;

public class ListFunction implements IFunction {

    private final int[] acceptedValues;

    public ListFunction(int[] acceptedValues) {
        this.acceptedValues = acceptedValues;
    }

    @Override
    public boolean canApplyFor(int input) {
        return Arrays.stream(acceptedValues).filter(value -> value == input).findFirst().isPresent();
    }

    static final class Adapter implements IFunctionAdapter<ListFunction> {

        @Override
        public ListFunction resolveFromJson(JsonObject object) throws JsonParseException {
            JsonArray array = JSONUtils.getAsJsonArray(object, "list");
            int[] accepted = new int[array.size()];
            int index = 0;
            for (JsonElement element : array) {
                accepted[index++] = element.getAsInt();
            }
            return new ListFunction(accepted);
        }
    }
}
