package com.wf.firearms.grenade;

/** 某一材质 + 种类组合后的爆炸参数。 */
public record GrenadeStats(
        GrenadeMaterial material,
        GrenadeKind kind,
        float blastRadius,
        float explosionDamage,
        boolean explodeOnImpact,
        int fuseTicks) {

    public static GrenadeStats of(GrenadeMaterial material, GrenadeKind kind) {
        float radius = material.baseBlastRadius() * kind.blastRadiusMultiplier();
        float damage = material.baseDamage() * kind.damageMultiplier();
        return new GrenadeStats(material, kind, radius, damage, kind.explodeOnImpact(), 60);
    }

    public String unlockSkill() {
        return kind.unlockSkill();
    }
}
