package com.wf.firearms.client.compat.tacz;

import net.minecraft.client.Minecraft;

/** 客户端 TaCZ 排障 HUD 用的起止 tick。 */
public final class TaczUnjamClientState {
    private static long startTick = -1;
    private static long endTick = -1;

    private TaczUnjamClientState() {}

    public static void begin(long start, long end) {
        startTick = start;
        endTick = end;
    }

    public static void clear() {
        startTick = -1;
        endTick = -1;
    }

    public static boolean isActive() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || endTick <= startTick) {
            return false;
        }
        long now = mc.level.getGameTime();
        if (now >= endTick) {
            clear();
            return false;
        }
        return true;
    }

    public static float progress() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || endTick <= startTick) {
            return 0f;
        }
        long now = mc.level.getGameTime();
        return Math.min(1f, Math.max(0f, (float) (now - startTick) / (endTick - startTick)));
    }
}
