package dev.toma.gunsrpg.common.perk;

public enum PerkType {

    BUFF,
    DEBUFF;

    public float getValue(PerkValueSpec spec) {
        return this == BUFF ? spec.buff() : spec.debuff();
    }
}
