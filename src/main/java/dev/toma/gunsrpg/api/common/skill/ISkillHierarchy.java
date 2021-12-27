package dev.toma.gunsrpg.api.common.skill;

import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;

public interface ISkillHierarchy<S extends ISkill> {

    SkillCategory getCategory();

    SkillType<?> getParent();

    SkillType<?>[] getChildren();

    SkillType<S> getOverride();

    boolean isContainer();
}
