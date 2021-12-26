package dev.toma.gunsrpg.common.skills.core;

public enum SkillCategory {

    RESISTANCE(),
    MINING(),
    SURVIVAL(),
    GUN(),
    ATTACHMENT(true);

    private final boolean internal;

    SkillCategory() {
        this(false);
    }

    SkillCategory(boolean internal) {
        this.internal = internal;
    }

    public boolean isInternal() {
        return internal;
    }

    public static SkillCategory get(String key) {
        String lookupKey = key.toUpperCase();
        return valueOf(lookupKey);
    }
}
