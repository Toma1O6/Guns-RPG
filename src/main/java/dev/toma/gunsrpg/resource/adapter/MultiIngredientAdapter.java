package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.resource.MultiIngredient;

import java.lang.reflect.Type;

public class MultiIngredientAdapter implements JsonDeserializer<MultiIngredient> {

    @Override
    public MultiIngredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return MultiIngredient.parseJson(json);
    }
}
