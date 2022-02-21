package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.crate.CountFunctionRegistry;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.gunsrpg.resource.crate.ICountFunctionAdapter;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class CountFunctionAdapter implements JsonDeserializer<ICountFunction> {

    @Override
    public ICountFunction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        String identifierString = JSONUtils.getAsString(object, "function", null);
        if (ModUtils.isNullOrEmpty(identifierString)) {
            throw new JsonParseException("Invalid property value: 'function' must be defined and it's value cannot be empty");
        }
        ResourceLocation identifier = new ResourceLocation(identifierString);
        ICountFunctionAdapter adapter = CountFunctionRegistry.findByKey(identifier);
        if (adapter == null) {
            throw new JsonParseException(String.format("Invalid property value 'function': '%s' is not valid function", identifierString));
        }
        return adapter.deserialize(object);
    }
}
