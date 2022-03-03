package dev.toma.gunsrpg.common.item.perk;

import java.util.Locale;

public enum PerkVariant {
    BLACK,
    BLUE,
    GREEN,
    RED,
    WHITE,
    YELLOW;

    public String getRegistryName(String type) {
        return name().toLowerCase(Locale.ROOT) + "_" + type;
    }
}
