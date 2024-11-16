package dev.toma.gunsrpg.api.common.data;

public final class DataFlags {

    public static final int WILDCARD    = -1;
    public static final int AIM         = 1;
    public static final int SKILLS      = 1 << 1;
    public static final int QUESTS      = 1 << 2;
    public static final int DEBUFF      = 1 << 3;
    public static final int ATTRIB      = 1 << 4;
    public static final int DATA        = 1 << 5;
    public static final int JAMS        = 1 << 6;
    public static final int PERK        = 1 << 7;
    public static final int WEAPON_POOL = 1 << 8;

    public static boolean isWildcard(int flags) {
        return flags == WILDCARD;
    }

    public static boolean is(int flag, int flags) {
        return isWildcard(flags) || (flags & flag) == flag;
    }

    private DataFlags() {}
}
