package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;

public class SkillHierarchy<S extends ISkill> implements ISkillHierarchy<S> {

    private final SkillCategory category;
    private final SkillType<?> parent;
    private final SkillType<?>[] children;
    private final SkillType<S> override;
    private final boolean isContainer;

    public SkillHierarchy(SkillCategory category, SkillType<?> parent, SkillType<?>[] children, SkillType<S> override, boolean isContainer) {
        this.category = category;
        this.parent = parent;
        this.children = children;
        this.override = override;
        this.isContainer = isContainer;
    }

    @Override
    public SkillCategory getCategory() {
        return category;
    }

    @Override
    public SkillType<?> getParent() {
        return parent;
    }

    @Override
    public SkillType<?>[] getChildren() {
        return children;
    }

    @Override
    public SkillType<S> getOverride() {
        return override;
    }

    @Override
    public boolean isContainer() {
        return isContainer;
    }
}
