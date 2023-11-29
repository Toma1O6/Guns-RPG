package dev.toma.gunsrpg.resource.ammobench;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public final class AmmoBenchOutputModifier {

    private final SkillType<?> skill;
    private final boolean skillActive;
    private final AmmoBenchOutputCount outputCount;

    public AmmoBenchOutputModifier(SkillType<?> skill, boolean skillActive, AmmoBenchOutputCount outputCount) {
        this.skill = skill;
        this.skillActive = skillActive;
        this.outputCount = outputCount;
    }

    public boolean canApply(ISkillProvider skillProvider) {
        boolean hasSkill = skillProvider.hasSkill(skill);
        return skillActive == hasSkill;
    }

    public int getResultCount(int count) {
        return outputCount.getCount(count);
    }

    public AmmoBenchOutputCount getCountFunction() {
        return outputCount;
    }

    public static AmmoBenchOutputModifier parseJson(JsonElement element) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(element);
        ResourceLocation skillId = new ResourceLocation(JSONUtils.getAsString(object, "skill"));
        SkillType<?> type = ModRegistries.SKILLS.getValue(skillId);
        if (type == null) {
            throw new JsonSyntaxException("Unknown skill: " + skillId);
        }
        boolean required = JSONUtils.getAsBoolean(object, "active", true);
        AmmoBenchOutputCount count = AmmoBenchOutputCountType.parseJson(JSONUtils.getAsJsonObject(object, "modifier"));
        return new AmmoBenchOutputModifier(type, required, count);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(skill.getRegistryName());
        buffer.writeBoolean(skillActive);
        AmmoBenchOutputCountType.toNetwork(outputCount, buffer);
    }

    public static AmmoBenchOutputModifier decode(PacketBuffer buffer) {
        ResourceLocation skillId = buffer.readResourceLocation();
        SkillType<?> skillType = ModRegistries.SKILLS.getValue(skillId);
        boolean active = buffer.readBoolean();
        AmmoBenchOutputCount count = AmmoBenchOutputCountType.fromNetwork(buffer);
        return new AmmoBenchOutputModifier(skillType, active, count);
    }
}
