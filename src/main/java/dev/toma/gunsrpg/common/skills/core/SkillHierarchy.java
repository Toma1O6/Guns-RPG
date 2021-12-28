package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;

public class SkillHierarchy implements ISkillHierarchy {

    private final SkillCategory category;
    private final SkillType<?> parent;
    private final SkillType<?>[] children;
    private final SkillType<?>[] extensions;

    public SkillHierarchy(SkillCategory category, SkillType<?> parent, SkillType<?>[] children, SkillType<?>[] extensions) {
        this.category = category;
        this.parent = parent;
        this.children = children;
        this.extensions = extensions;
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
    public SkillType<?>[] getExtensions() {
        return extensions;
    }
}
