package dev.toma.gunsrpg.common.skills.core;

import java.util.Locale;
import java.util.function.Supplier;

public enum SkillCategory {

    RESISTANCE(),
    MINING(),
    SURVIVAL(),
    GUN(),
    ATTACHMENT(() -> GUN);

    private final Supplier<SkillCategory> parent;

    SkillCategory() {
        this(() -> null);
    }

    SkillCategory(Supplier<SkillCategory> parent) {
        this.parent = parent;
    }

    public static SkillCategory get(String key) {
        String lookupKey = key.toUpperCase(Locale.ROOT);
        return valueOf(lookupKey);
    }

    public SkillCategory getParent() {
        return parent.get();
    }

    @Deprecated
    public static SkillCategory[] mainCategories() {
        return values();
    }

    @Deprecated
    public boolean isChild() {
        return false;
    }

    @Deprecated
    public boolean hasChild() {
        return false;
    }

    @Deprecated
    public SkillCategory getChild() {
        return null;
    }
}
