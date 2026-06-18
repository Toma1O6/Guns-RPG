package com.wf.firearms.grenade;

/** 手雷种类（破片 ≠ 重型：前者扩范围，后者缩范围、抬伤害）。 */
public enum GrenadeKind {
    STANDARD("", "grenades", 1.0f, 1.0f, false),
    FRAGMENTATION("fragmentation_", "impact_grenades", 1.5f, 1.6f, false),
    HEAVY("heavy_", "massive_grenades", 0.75f, 2.5f, false);

    private final String itemPrefix;
    private final String unlockSkill;
    private final float blastRadiusMultiplier;
    private final float damageMultiplier;
    private final boolean explodeOnImpact;

    GrenadeKind(
            String itemPrefix,
            String unlockSkill,
            float blastRadiusMultiplier,
            float damageMultiplier,
            boolean explodeOnImpact) {
        this.itemPrefix = itemPrefix;
        this.unlockSkill = unlockSkill;
        this.blastRadiusMultiplier = blastRadiusMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.explodeOnImpact = explodeOnImpact;
    }

    public String itemPrefix() {
        return itemPrefix;
    }

    public String unlockSkill() {
        return unlockSkill;
    }

    public float blastRadiusMultiplier() {
        return blastRadiusMultiplier;
    }

    public float damageMultiplier() {
        return damageMultiplier;
    }

    public boolean explodeOnImpact() {
        return explodeOnImpact;
    }

    /** 如 {@code grenade_iron}、{@code fragmentation_grenade_gold}、{@code heavy_grenade_iron}。 */
    public String itemId(String materialId) {
        if (this == STANDARD) {
            return "grenade_" + materialId;
        }
        return itemPrefix + "grenade_" + materialId;
    }

    /** 兼容旧物品 id：{@code grenade}、{@code impact_grenade}、{@code massive_grenade}（铁）。 */
    public String legacyItemId() {
        return switch (this) {
            case STANDARD -> "grenade";
            case FRAGMENTATION -> "impact_grenade";
            case HEAVY -> "massive_grenade";
        };
    }
}
