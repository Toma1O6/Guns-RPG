package dev.toma.gunsrpg.resource.util.conditions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;

public class SkillCondition implements IRecipeCondition {

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
        ISkillProvider provider = dataOptional.orElseThrow(IllegalStateException::new).getSkillProvider();
        return provider.hasSkill(skill);
    }

    @Override
    public ConditionType<?> getType() {
        return Conditions.SKILL_CONDITION;
    }

    @Override
    public ITextComponent getDisplayInfo() {
        return new TranslationTextComponent("condition.crafting.skill", skill.getTitle());
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

        @Override
        public void toNetwork(PacketBuffer buffer, SkillCondition condition) {

        }

        @Override
        public SkillCondition fromNetwork(PacketBuffer buffer) {
            return null;
        }
    }
}
