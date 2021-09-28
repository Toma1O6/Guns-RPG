package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.data.ISkills;
import dev.toma.gunsrpg.common.capability.PlayerData;
import net.minecraft.entity.player.PlayerEntity;

@SuppressWarnings("unchecked")
public class SkillUtil {

    public static <S extends ISkill> S getBestSkillFromOverrides(S skill, PlayerEntity player) {
        ISkills skills = PlayerData.get(player).orElseThrow(NullPointerException::new).getSkills();
        if (skill == null)
            return null;
        while (skill.getType().isOverriden() && skills.hasSkill(skill.getType().getOverride())) {
            skill = (S) skills.getSkill(skill.getType().getOverride());
        }
        return skill;
    }
}
