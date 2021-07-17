package dev.toma.gunsrpg.common.skills.core;

import java.util.function.Supplier;

public enum SkillCategory {

    RESISTANCE(false),
    MINING(false),
    SURVIVAL(true),
    ATTACHMENT(false, true),
    GUN(true, () -> ATTACHMENT, false);

    private static SkillCategory[] tickableTypes;
    private static SkillCategory[] mainCategories;
    private final boolean containsTickableTypes;
    private final Supplier<SkillCategory> childCategory;
    private final boolean isChild;

    SkillCategory(boolean containsTickableTypes) {
        this(containsTickableTypes, false);
    }

    SkillCategory(boolean containsTickableTypes, boolean isChild) {
        this(containsTickableTypes, null, isChild);
    }

    SkillCategory(boolean containsTickableTypes, Supplier<SkillCategory> childCategory, boolean isChild) {
        this.containsTickableTypes = containsTickableTypes;
        this.childCategory = childCategory;
        this.isChild = isChild;
    }

    public static SkillCategory[] mainCategories() {
        if (mainCategories == null) {
            SkillCategory[] temp = new SkillCategory[values().length];
            int c = 0;
            for (int i = values().length - 1; i >= 0; i--) {
                SkillCategory category = values()[i];
                if (!category.isChild()) {
                    temp[c] = category;
                    ++c;
                }
            }
            mainCategories = new SkillCategory[c];
            System.arraycopy(temp, 0, mainCategories, 0, c);
        }
        return mainCategories;
    }

    public static SkillCategory[] tickables() {
        if (tickableTypes == null) {
            SkillCategory[] temp = new SkillCategory[values().length];
            int c = 0;
            for (SkillCategory category : values()) {
                if (category.containsTickableTypes) {
                    temp[c] = category;
                    ++c;
                }
            }
            tickableTypes = new SkillCategory[c];
            System.arraycopy(temp, 0, tickableTypes, 0, c);
        }
        return tickableTypes;
    }

    public boolean isChild() {
        return isChild;
    }

    public boolean hasChild() {
        return childCategory != null;
    }

    public SkillCategory getChild() {
        return childCategory.get();
    }
}
