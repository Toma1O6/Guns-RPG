package com.wf.firearms.client;

/** 准星样式/颜色（对标 Guns RPG sight_type / sight_color，纯客户端）。 */
public final class FirearmCrosshairStyle {
    public static final int[] COLORS = {
            0xFFFFFFFF, 0xFF55FF55, 0xFFFF5555, 0xFFFFFF55, 0xFF55FFFF
    };
    public static final String[] TYPE_KEYS = {"dot", "cross", "circle", "bracket"};

    private static int colorIndex;
    private static int typeIndex;

    private FirearmCrosshairStyle() {}

    public static int colorArgb() {
        return COLORS[Math.floorMod(colorIndex, COLORS.length)];
    }

    public static String typeKey() {
        return TYPE_KEYS[Math.floorMod(typeIndex, TYPE_KEYS.length)];
    }

    public static void cycleColor() {
        colorIndex++;
    }

    public static void cycleType() {
        typeIndex++;
    }
}
