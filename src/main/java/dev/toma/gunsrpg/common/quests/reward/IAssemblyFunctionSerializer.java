package dev.toma.gunsrpg.common.quests.reward;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public interface IAssemblyFunctionSerializer {

    IAssemblyFunction deserialize(JsonElement element, JsonDeserializationContext context) throws JsonParseException;
}
