package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.util.ModUtils;

public enum Firemode {

    SINGLE("Single"),
    BURST("Burst"),
    FULL_AUTO("Full Auto");

    private final String name;

    Firemode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Firemode get(int id) {
        int i = ModUtils.wrap(id, 0, 2);
        return values()[i];
    }
}
