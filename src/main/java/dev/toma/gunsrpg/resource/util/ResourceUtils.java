package dev.toma.gunsrpg.resource.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.resource.util.conditions.ConditionType;
import dev.toma.gunsrpg.resource.util.conditions.Conditions;
import dev.toma.gunsrpg.resource.util.conditions.IConditionSerializer;
import dev.toma.gunsrpg.resource.util.conditions.IRecipeCondition;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public final class ResourceUtils {

    public static List<IRecipeCondition> getConditionsFromJson(JsonArray array) throws JsonSyntaxException {
        List<IRecipeCondition> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            IRecipeCondition condition = resolveCondition(element);
            list.add(condition);
        }
        return list;
    }

    public static <C extends IRecipeCondition> C resolveCondition(JsonElement element) throws JsonSyntaxException {
        JsonObject obj = JsonHelper.asJsonObject(element);
        String id = JSONUtils.getAsString(obj, "type");
        ResourceLocation location = new ResourceLocation(id);
        ConditionType<C> type = Conditions.find(location);
        if (type == null) {
            throw new JsonSyntaxException("Unknown recipe condition type: " + id);
        }
        JsonObject predicate = JSONUtils.getAsJsonObject(obj, "predicate");
        IConditionSerializer<C> serializer = type.getSerializer();
        return serializer.deserialize(predicate);
    }
}
