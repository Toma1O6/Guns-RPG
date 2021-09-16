package dev.toma.gunsrpg.common.quests.serialization;

import com.google.gson.*;
import dev.toma.gunsrpg.common.quests.IQuest;
import dev.toma.gunsrpg.common.quests.Quest;
import dev.toma.gunsrpg.common.quests.condition.ConditionList;
import dev.toma.gunsrpg.common.quests.condition.ConditionListManager;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class QuestLoader {

    public IQuest load(ResourceLocation questID, JsonObject data, LoadingContext context) throws JsonParseException {
        Set<IQuestCondition> conditions = data.has("conditions") ? readConditions(JSONUtils.getAsJsonArray(data, "conditions"), context) : Collections.emptySet();

        return new Quest(questID, conditions, 0);
    }

    private Set<IQuestCondition> readConditions(JsonArray conditions, LoadingContext context) throws JsonParseException {
        Set<IQuestCondition> set = new HashSet<>(conditions.size());
        for (JsonElement element : conditions) {
            set.add(readCondition(element, context));
        }
        return set;
    }

    private IQuestCondition readCondition(JsonElement element, LoadingContext context) throws JsonParseException {
        if (element.isJsonObject())
            throw new JsonSyntaxException("Expected JsonObject, got " + element.getClass().getSimpleName());
        JsonObject object = element.getAsJsonObject();
        String type = JSONUtils.getAsString(object, "type");
        ConditionStorageType storageType;
        try {
            storageType = ConditionStorageType.valueOf(type);
        } catch (IllegalArgumentException iae) {
            throw new JsonSyntaxException(iae.getMessage());
        }
        switch (storageType) {
            case LIST:
                return readConditionFromList(object, context.getListManager(), context.getConditionManager());
            case CONDITION:
                return readSingleConditionEntry(object, context.getConditionManager());
            default:
                throw new JsonSyntaxException("Unknown condition type " + storageType);
        }
    }

    private IQuestCondition readConditionFromList(JsonObject dataObj, ConditionListManager listManager, QuestConditionManager conditionManager) throws JsonParseException {
        String path = JSONUtils.getAsString(dataObj, "path");
        ResourceLocation resourcePath = new ResourceLocation(path);
        ConditionList list = listManager.getValue(resourcePath);
        if (list == null) {
            throw new JsonSyntaxException("Unknown condition list " + path);
        }
        if (dataObj.has("excl")) { // exclusions
            JsonArray array = JSONUtils.getAsJsonArray(dataObj, "excl");
            Set<IQuestCondition> excluded = new HashSet<>(array.size());
            for (JsonElement element : array) {
                excluded.add(findCondition(new ResourceLocation(element.getAsString()), conditionManager));
            }
            return list.selectRandom(excluded);
        } else {
            return list.selectRandom();
        }
    }

    private IQuestCondition readSingleConditionEntry(JsonObject dataObj, QuestConditionManager conditionManager) throws JsonParseException {
        String path = JSONUtils.getAsString(dataObj, "path");
        return findCondition(new ResourceLocation(path), conditionManager);
    }

    private IQuestCondition findCondition(ResourceLocation key, QuestConditionManager manager) throws JsonParseException {
        IQuestCondition condition = manager.getValue(key);
        if (condition == null) {
            throw new JsonSyntaxException("Unknown condition " + key.toString());
        }
        return condition;
    }

    public enum ConditionStorageType {
        LIST,
        CONDITION;
    }
}
