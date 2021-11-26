package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.util.IFlags;
import dev.toma.gunsrpg.util.ModUtils;

public enum Firemode {

    SINGLE("Single", FiremodeHandlerFlags.ON_INPUT),
    BURST("Burst", FiremodeHandlerFlags.ON_INPUT, FiremodeHandlerFlags.ON_TICK),
    FULL_AUTO("Full Auto", FiremodeHandlerFlags.ON_TICK),
    SINGLE_BARREL("Single Barrel", FiremodeHandlerFlags.ON_INPUT),
    BOTH_BARRELS("Both Barrels", FiremodeHandlerFlags.ON_INPUT),
    ALL("All", FiremodeHandlerFlags.ON_INPUT);

    private final String name;
    private final int handlers;

    Firemode(String name, FiremodeHandlerFlags... handlers) {
        this.name = name;
        this.handlers = IFlags.combine(FiremodeHandlerFlags::getFlag, handlers);
    }

    public boolean isSubscribedTo(FiremodeHandlerFlags event) {
        int id = event.getFlag();
        return (handlers & id) == id;
    }

    public static Firemode get(int id) {
        Firemode[] modes = values();
        int i = ModUtils.clamp(id, 0, modes.length - 1);
        return modes[i];
    }

    public String getName() {
        return name;
    }
}
