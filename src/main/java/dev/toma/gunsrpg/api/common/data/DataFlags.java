package dev.toma.gunsrpg.api.common.data;

public final class DataFlags {

    public static final int WILDCARD    = 1;
    public static final int AIM         = 1 << 1;
    public static final int SKILLS      = 1 << 2;
    public static final int QUESTS      = 1 << 3;
    public static final int DEBUFF      = 1 << 4;
    public static final int ATTRIB      = 1 << 5;
    public static final int DATA        = 1 << 6;

    public static boolean isWildcard(int value) {
        return value == -1;
    }

    public static boolean is(int flag, int flags) {
        return (flags & flag) == flag;
    }

    private DataFlags() {}
}
