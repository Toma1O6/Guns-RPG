package dev.toma.gunsrpg.common.perk;

public final class PerkValueSpec {

    private final float buffValue;
    private final float debuffValue;

    public PerkValueSpec(float buffValue, float debuffValue) {
        this.buffValue = buffValue;
        this.debuffValue = debuffValue;
    }

    public float buff() {
        return buffValue;
    }

    public float debuff() {
        return debuffValue;
    }
}
