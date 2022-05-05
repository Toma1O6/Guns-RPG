package dev.toma.gunsrpg.common.item.perk;

import java.util.Locale;

public enum PerkVariant {

    BLUE(0x4444ff),
    GREEN(0xaa << 8),
    RED(0xaa << 16),
    YELLOW(0xeeee << 8),
    WHITE(0xeeeeee),
    BLACK(0x404040);

    final int rgb;

    PerkVariant(int rgb) {
        this.rgb = rgb;
    }

    public int getRgb() {
        return rgb;
    }

    public String getRegistryName(String type) {
        return name().toLowerCase(Locale.ROOT) + "_" + type;
    }

    public static PerkVariant byId(int id) {
        PerkVariant[] variants = values();
        return variants[id % variants.length];
    }
}
