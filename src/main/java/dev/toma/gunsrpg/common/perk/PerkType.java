package dev.toma.gunsrpg.common.perk;

public enum PerkType {

    BUFF,
    DEBUFF;

    public static PerkType byValue(int value) {
        return value < 0 ? DEBUFF : BUFF;
    }

    public int getLevel(int level) {
        return this == BUFF ? level : -level;
    }

    public float getValue(PerkValueSpec spec) {
        return this == BUFF ? spec.buff() : spec.debuff();
    }
}
