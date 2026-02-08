package dev.toma.gunsrpg.common.quests.condition.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionLoader;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public final class WeightedProvider {

    private final int weight;
    private final ResourceLocation id;
    private final IQuestConditionProvider<?> provider;
    private final List<SkillType<?>> requiredSkills;

    public WeightedProvider(int weight, ResourceLocation id, IQuestConditionProvider<?> provider, List<SkillType<?>> requiredSkills) {
        this.weight = weight;
        this.id = id;
        this.provider = provider;
        this.requiredSkills = requiredSkills;
    }

    public int getWeight() {
        return weight;
    }

    public ResourceLocation getId() {
        return id;
    }

    public IQuestConditionProvider<?> getProvider() {
        return provider;
    }

    public boolean canUse(PlayerEntity player) {
        for (SkillType<?> skill : this.requiredSkills) {
            if (!PlayerData.hasActiveSkill(player, skill)) {
                return false;
            }
        }
        return true;
    }

    public static WeightedProvider resolve(JsonElement element, QuestConditionLoader manager) {
        JsonObject object = JsonHelper.asJsonObject(element);
        int weight = JSONUtils.getAsInt(object, "weight");
        String id = JSONUtils.getAsString(object, "id", "no_name");
        JsonObject condition = JSONUtils.getAsJsonObject(object, "condition");
        JsonArray requiredSkills = JSONUtils.getAsJsonArray(object, "required_skills", new JsonArray());
        IQuestConditionProvider<?> provider = manager.loadCondition(condition);
        List<SkillType<?>> requirements = JsonHelper.deserializeAsList(requiredSkills, JsonHelper::resolveSkill);
        return new WeightedProvider(weight, new ResourceLocation(id), provider, requirements);
    }
}
