package com.wf.firearms.bloodmoon;

import com.wf.firearms.config.BloodmoonConfig;
import com.wf.firearms.network.BloodmoonSyncPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

/** 血月调度：每 N 游戏日夜间开启；支持指令强制。 */
public final class BloodmoonService {
    private static final long NIGHT_START = 12500L;
    private static final long TICKS_PER_DAY = 24000L;

    private BloodmoonService() {}

    public static boolean isBloodMoon(Level level) {
        if (!(level instanceof ServerLevel server)) {
            return false;
        }
        if (server.dimension() != Level.OVERWORLD) {
            return false;
        }
        return BloodmoonSaveData.get(server).isActive();
    }

    public static void tick(ServerLevel level) {
        if (level.dimension() != Level.OVERWORLD) {
            return;
        }
        BloodmoonSaveData data = BloodmoonSaveData.get(level);
        long dayPhase = level.getDayTime() % TICKS_PER_DAY;
        boolean isNight = dayPhase >= NIGHT_START;
        // 强制血月用于白天测试，但不应跨黎明残留到次日（否则玩家会以为血月「停不下来」）
        if (data.isForced() && !isNight && data.wasActive()) {
            data.setForced(false);
        }
        boolean shouldBeActive = computeShouldBeActive(level, data);
        boolean was = data.wasActive();
        if (shouldBeActive != was) {
            if (shouldBeActive) {
                onStart(level);
            } else {
                onEnd(level);
                data.setForced(false);
            }
            data.setWasActive(shouldBeActive);
            syncClients(level);
        }
        data.setActive(shouldBeActive);
    }

    public static void syncClients(ServerLevel level) {
        if (level.dimension() != Level.OVERWORLD) {
            return;
        }
        BloodmoonSaveData data = BloodmoonSaveData.get(level);
        BloodmoonSyncPacket.broadcast(level, data.isActive(), data.isForced());
    }

    private static boolean computeShouldBeActive(ServerLevel level, BloodmoonSaveData data) {
        if (data.isForced()) {
            return true;
        }
        int cycle = BloodmoonConfig.bloodmoonCycle();
        if (cycle < 0) {
            return false;
        }
        long dayTime = level.getDayTime();
        if (dayTime % TICKS_PER_DAY < NIGHT_START) {
            return false;
        }
        long day = dayTime / TICKS_PER_DAY;
        if (day <= 0) {
            return false;
        }
        return cycle == 0 || day % cycle == 0;
    }

    public static boolean forceStart(ServerLevel level) {
        if (level.dimension() != Level.OVERWORLD) {
            return false;
        }
        BloodmoonSaveData data = BloodmoonSaveData.get(level);
        if (!data.isActive()) {
            onStart(level);
            data.setWasActive(true);
            data.setActive(true);
        }
        data.setForced(true);
        syncClients(level);
        return true;
    }

    public static boolean forceStop(ServerLevel level) {
        if (level.dimension() != Level.OVERWORLD) {
            return false;
        }
        BloodmoonSaveData data = BloodmoonSaveData.get(level);
        data.setForced(false);
        boolean natural = computeShouldBeActive(level, data);
        if (data.isActive() && !natural) {
            onEnd(level);
        }
        data.setActive(natural);
        data.setWasActive(natural);
        syncClients(level);
        return true;
    }

    public static String statusLine(ServerLevel level) {
        BloodmoonSaveData data = BloodmoonSaveData.get(level);
        int cycle = BloodmoonConfig.bloodmoonCycle();
        long day = level.getDayTime() / TICKS_PER_DAY;
        long toNext = cycle <= 0 ? 0 : (cycle - (day % cycle)) % cycle;
        return "active="
                + data.isActive()
                + " forced="
                + data.isForced()
                + " day="
                + day
                + " cycle="
                + cycle
                + " nightsUntil="
                + toNext
                + " agro="
                + BloodmoonConfig.bloodMoonMobAgroRange();
    }

    private static void onStart(ServerLevel level) {
        for (ServerPlayer player : level.players()) {
            player.playNotifySound(
                    SoundEvents.END_PORTAL_SPAWN, SoundSource.NEUTRAL, 1.0f, 1.0f);
            player.sendSystemMessage(
                    Component.translatable("event.gunsrpg.bloodmoon.start"));
        }
    }

    private static void onEnd(ServerLevel level) {
        for (ServerPlayer player : level.players()) {
            player.sendSystemMessage(
                    Component.translatable("event.gunsrpg.bloodmoon.end").withStyle(net.minecraft.ChatFormatting.GREEN));
            player.playNotifySound(
                    SoundEvents.CAT_AMBIENT, SoundSource.NEUTRAL, 0.6f, 0.8f);
        }
    }
}
