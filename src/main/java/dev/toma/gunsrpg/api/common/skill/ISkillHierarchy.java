package dev.toma.gunsrpg.api.common.skill;

import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;

public interface ISkillHierarchy {

    SkillCategory getCategory();

    SkillType<?> getParent();

    SkillType<?>[] getChildren();

    SkillType<?>[] getExtensions();
}
