package dev.toma.gunsrpg.resource.gunner;

public final class GunnerGlobalProperties {

    private final float damageMultiplier;
    private final float inaccuracy;
    private final float accuracyBonus;

    public GunnerGlobalProperties(float damageMultiplier, float inaccuracy, float accuracyBonus) {
        this.damageMultiplier = damageMultiplier;
        this.inaccuracy = inaccuracy;
        this.accuracyBonus = accuracyBonus;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getInaccuracy() {
        return inaccuracy;
    }

    public float getAccuracyBonus() {
        return accuracyBonus;
    }
}
