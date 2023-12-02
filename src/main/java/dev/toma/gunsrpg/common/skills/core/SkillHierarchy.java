package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;

import java.util.Arrays;

public class SkillHierarchy<S extends ISkill> implements ISkillHierarchy<S> {

    private final SkillCategory category;
    private final SkillType<?> parent;
    private final SkillType<S> override;
    private final SkillType<?>[] children;
    private final SkillType<?>[] extensions;

    public SkillHierarchy(SkillCategory category, SkillType<?> parent, SkillType<S> override, SkillType<?>[] children, SkillType<?>[] extensions) {
        this.category = category;
        this.parent = parent;
        this.override = override;
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
    public SkillType<S> getOverride() {
        return override;
    }

    @Override
    public SkillType<?>[] getChildren() {
        return children != null ? Arrays.stream(children).filter(child -> !child.isDisabled()).toArray(SkillType[]::new) : null;
    }

    @Override
    public SkillType<?>[] getExtensions() {
        return extensions;
    }
}
