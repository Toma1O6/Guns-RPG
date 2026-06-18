package com.wf.firearms.client;

import com.wf.firearms.combat.FirearmMode;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.network.FirearmStateSyncPacket;
import net.minecraft.client.Minecraft;

/** 客户端 HUD 用的枪械状态缓存（由服务端同步包更新）。 */
public final class FirearmHudState {
    private static int ammo;
    private static int magSize;
    private static boolean jammed;
    private static FirearmMode fireMode = FirearmMode.SINGLE;
    private static FirearmStackState.Action action = FirearmStackState.Action.NONE;
    private static long actionStart;
    private static long actionEnd;

    private FirearmHudState() {}

    public static void update(FirearmStateSyncPacket packet) {
        ammo = packet.ammo();
        magSize = packet.magSize();
        jammed = packet.jammed();
        fireMode = packet.fireMode();
        action = packet.action();
        actionStart = packet.actionStart();
        actionEnd = packet.actionEnd();
    }

    public static int ammo() {
        return ammo;
    }

    public static int magSize() {
        return magSize;
    }

    public static boolean jammed() {
        return jammed;
    }

    public static FirearmMode fireMode() {
        return fireMode;
    }

    public static FirearmStackState.Action action() {
        return action;
    }

    public static float actionProgress() {
        if (action == FirearmStackState.Action.NONE) {
            return 0f;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return 0f;
        }
        long now = mc.level.getGameTime();
        long span = Math.max(1, actionEnd - actionStart);
        return Math.min(1f, Math.max(0f, (float) (now - actionStart) / span));
    }

    public static boolean hasData() {
        return magSize > 0;
    }
}
