package dev.toma.gunsrpg.common.quests.adapters;

import com.google.gson.*;
import dev.toma.gunsrpg.common.quests.reward.AssemblyFunctions;
import dev.toma.gunsrpg.common.quests.reward.IAssemblyFunction;
import dev.toma.gunsrpg.common.quests.reward.IAssemblyFunctionSerializer;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class AssemblyFunctionAdapter implements JsonDeserializer<IAssemblyFunction> {

    @Override
    public IAssemblyFunction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        ResourceLocation funcId = new ResourceLocation(JSONUtils.getAsString(object, "type"));
        IAssemblyFunctionSerializer serializer = AssemblyFunctions.getSerializer(funcId);
        if (serializer == null) throw new JsonSyntaxException("Unknown assembly function: " + funcId);
        return serializer.deserialize(json, context);
    }
}
