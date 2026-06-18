package com.wf.firearms.combat;

/** 开火模式（对标 Guns RPG 半自动/全自动切换）。 */
public enum FirearmMode {
    SINGLE,
    AUTO;

    public FirearmMode toggle(boolean supportsAuto) {
        if (!supportsAuto) {
            return SINGLE;
        }
        return this == SINGLE ? AUTO : SINGLE;
    }

    public static FirearmMode fromOrdinal(int ord) {
        FirearmMode[] values = values();
        if (ord < 0 || ord >= values.length) {
            return SINGLE;
        }
        return values[ord];
    }
}
