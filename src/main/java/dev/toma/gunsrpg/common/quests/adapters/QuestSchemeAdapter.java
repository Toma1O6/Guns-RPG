package dev.toma.gunsrpg.common.quests.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionLoader;
import dev.toma.gunsrpg.common.quests.quest.*;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class QuestSchemeAdapter {

    private final QuestConditionLoader loader;

    public QuestSchemeAdapter(QuestConditionLoader loader) {
        this.loader = loader;
    }

    public <D extends IQuestData> QuestScheme<D> resolveQuestFile(ResourceLocation filePath, JsonElement element) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(element);
        ResourceLocation questTypeId = new ResourceLocation(JSONUtils.getAsString(object, "type"));
        QuestType<D> questType = QuestTypes.getTypeById(questTypeId);
        if (questType == null) {
            throw new JsonSyntaxException("Unknown quest type: " + questTypeId);
        }
        int tier = JSONUtils.getAsInt(object, "tier");
        if (tier < 1) {
            throw new JsonSyntaxException("Invalid quest tier: " + tier + ", must be bigger than 0");
        }
        DisplayInfo displayInfo = DisplayInfo.fromJson(JSONUtils.getAsJsonObject(object, "display"));
        D data = questType.resolveJson(object.get("data"));
        IQuestConditionProvider[] conditions = object.has("conditions") ?
                JsonHelper.deserializeInto(JSONUtils.getAsJsonArray(object, "conditions"), IQuestConditionProvider[]::new, loader::loadCondition) :
                new IQuestConditionProvider[0];
        QuestConditionTierScheme tierScheme = QuestConditionTierScheme.EMPTY_SCHEME;
        if (object.has("tieredConditions")) {
            JsonObject tieredConditions = JSONUtils.getAsJsonObject(object, "tieredConditions");
            tierScheme = QuestConditionTierScheme.fromJson(tieredConditions);
        }
        return new QuestScheme<>(filePath, questType, data, tier, displayInfo, conditions, tierScheme);
    }
}
