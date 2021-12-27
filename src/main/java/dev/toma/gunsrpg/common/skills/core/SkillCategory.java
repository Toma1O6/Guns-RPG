package dev.toma.gunsrpg.common.skills.core;

public enum SkillCategory {

    RESISTANCE,
    MINING,
    SURVIVAL,
    GUN;

    public static SkillCategory get(String key) {
        String lookupKey = key.toUpperCase();
        return valueOf(lookupKey);
    }
}
