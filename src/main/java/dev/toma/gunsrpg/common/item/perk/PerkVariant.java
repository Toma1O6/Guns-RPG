package dev.toma.gunsrpg.common.item.perk;

import java.util.Locale;

public enum PerkVariant {

    BLUE,
    GREEN,
    RED,
    YELLOW,
    WHITE,
    BLACK;

    public String getRegistryName(String type) {
        return name().toLowerCase(Locale.ROOT) + "_" + type;
    }

    public static PerkVariant byId(int id) {
        PerkVariant[] variants = values();
        return variants[id % variants.length];
    }
}
