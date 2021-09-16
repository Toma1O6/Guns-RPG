package dev.toma.gunsrpg.util;

public interface IFlags {

    boolean is(int flags);

    static int value(int index) {
        return 1 << index;
    }
}
