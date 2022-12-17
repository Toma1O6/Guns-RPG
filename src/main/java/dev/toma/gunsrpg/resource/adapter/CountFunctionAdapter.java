package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.resource.crate.CountFunctionRegistry;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.gunsrpg.resource.util.functions.IFunction;

import java.lang.reflect.Type;

public class CountFunctionAdapter implements JsonDeserializer<ICountFunction> {

    private final IFunction range;

    public CountFunctionAdapter(IFunction range) {
        this.range = range;
    }

    public static CountFunctionAdapter positive() {
        return new CountFunctionAdapter(input -> input >= 0);
    }

    @Override
    public ICountFunction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return CountFunctionRegistry.INSTANCE.fromJson(json, this.range);
    }
}
