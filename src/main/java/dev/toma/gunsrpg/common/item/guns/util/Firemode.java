package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.util.ModUtils;

public enum Firemode {

    SINGLE,
    BURST,
    FULL_AUTO;

    public static Firemode get(int id) {
        int i = ModUtils.wrap(id, 0, 2);
        return values()[i];
    }
}
