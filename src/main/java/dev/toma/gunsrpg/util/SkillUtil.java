package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.data.ISkills;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;

@SuppressWarnings("unchecked")
public class SkillUtil {

    public static <S extends ISkill> S getOverride(S skill, ISkills skills) {
        SkillType<S> type = (SkillType<S>) skill.getType();
        ISkillHierarchy<S> hierarchy = type.getHierarchy();
        SkillType<S> override = hierarchy.getOverride();
        if (skills.hasSkill(override)) {
            return getOverride(skills.getSkill(override), skills);
        } else {
            return skill;
        }
    }
}
