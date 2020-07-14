package dev.toma.gunsrpg.common.skills.core;

public enum SkillCategory {

    GUN(true),
    DEBUFF(false),
    MINING(false),
    SURVIVAL(true),
    ATTACHMENT(false);

    private static SkillCategory[] tickableTypes;
    private final boolean containsTickableTypes;

    SkillCategory(boolean containsTickableTypes) {
        this.containsTickableTypes = containsTickableTypes;
    }

    public static SkillCategory[] tickables() {
        if(tickableTypes == null) {
            SkillCategory[] temp = new SkillCategory[values().length];
            int c = 0;
            for(SkillCategory category : values()) {
                if(category.containsTickableTypes) {
                    temp[c] = category;
                    ++c;
                }
            }
            tickableTypes = new SkillCategory[c];
            System.arraycopy(temp, 0, tickableTypes, 0, c);
        }
        return tickableTypes;
    }
}
