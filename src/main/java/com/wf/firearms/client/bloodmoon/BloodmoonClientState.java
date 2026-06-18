package com.wf.firearms.client.bloodmoon;

/** 客户端血月状态（由 {@link com.wf.firearms.network.BloodmoonSyncPacket} 更新）。 */
public final class BloodmoonClientState {
    private static boolean active;
    private static boolean forced;

    private BloodmoonClientState() {}

    public static void set(boolean active, boolean forced) {
        BloodmoonClientState.active = active;
        BloodmoonClientState.forced = forced;
    }

    public static boolean isActive() {
        return active;
    }

    public static boolean isForced() {
        return forced;
    }

    /** 雾效/色调强度：强制血月白天也可见，自然血月按黄昏渐变。 */
    public static float visualIntensity(long dayTime) {
        if (!active) {
            return 0f;
        }
        if (forced) {
            return 0.85f;
        }
        long t = dayTime % 24000L;
        float diff = 1f;
        if (t < 14000L) {
            diff = (t - 12500L) / 1500f;
        } else if (t > 22500L) {
            diff = 1f - (t - 22500L) / 1500f;
        }
        return Math.max(0f, Math.min(1f, diff));
    }
}
