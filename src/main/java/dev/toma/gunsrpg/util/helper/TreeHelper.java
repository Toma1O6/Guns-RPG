package dev.toma.gunsrpg.util.helper;

import java.util.function.Function;

public class TreeHelper {

    public static <T> int getChildCount(T treeElement, Function<T, T[]> childrenFunc) {
        T[] children = childrenFunc.apply(treeElement);
        int count = 1;
        if (children == null) {
            return 0;
        } else {
            for (T child : children) {
                count += getChildCount(child, childrenFunc);
            }
        }
        return count;
    }
}
