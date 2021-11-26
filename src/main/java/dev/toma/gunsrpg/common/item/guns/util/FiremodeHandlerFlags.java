package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.util.IFlags;

public enum FiremodeHandlerFlags implements IFlags {

    ON_INPUT,
    ON_TICK;

    private final int flag;

    FiremodeHandlerFlags() {
        this.flag = 1 << ordinal();
    }

    public int getFlag() {
        return flag;
    }

    @Override
    public boolean is(int flags) {
        return (flags & flag) == flag;
    }
}
