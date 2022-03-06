package dev.toma.gunsrpg.common.item.perk;

import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkType;

public final class CrystalAttribute {

    private final Perk perk;
    private final PerkType type;
    private final int level;

    public CrystalAttribute(Perk perk, PerkType type, int level) {
        this.perk = perk;
        this.type = type;
        this.level = level;
    }

    public Perk getPerk() {
        return perk;
    }

    public PerkType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }
}
