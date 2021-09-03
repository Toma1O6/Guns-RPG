package dev.toma.gunsrpg.util.recipes.smithing;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;

public class SkillCondition implements ICraftingCondition {

    private final SkillType<?> skill;

    public SkillCondition(SkillType<?> skill) {
        this.skill = skill;
    }

    @Override
    public boolean canCraft(PlayerEntity player) {
        LazyOptional<IPlayerData> dataOptional = PlayerData.get(player);
        if (!dataOptional.isPresent()) {
            return false;
        }
        PlayerSkills skills = dataOptional.orElseThrow(IllegalStateException::new).getSkills();
        return skills.hasSkill(skill);
    }

    @Override
    public ConditionType<?> getType() {
        return Conditions.SKILL_CONDITION;
    }

    public static class Serializer implements IConditionSerializer<SkillCondition> {

        @Override
        public SkillCondition deserialize(JsonObject data) throws JsonParseException {
            String id = JSONUtils.getAsString(data, "skill");
            ResourceLocation location = new ResourceLocation(id);
            SkillType<?> skillType = ModRegistries.SKILLS.getValue(location);
            if (skillType == null)
                throw new JsonSyntaxException("Unknown skill: " + id);
            return new SkillCondition(skillType);
        }
    }
}
