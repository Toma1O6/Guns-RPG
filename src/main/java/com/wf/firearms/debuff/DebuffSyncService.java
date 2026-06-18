package com.wf.firearms.debuff;

import com.wf.firearms.network.DebuffSyncPacket;
import net.minecraft.server.level.ServerPlayer;

/** 服务端：计算 debuff 进度并推送到客户端 HUD。 */
public final class DebuffSyncService {
    private DebuffSyncService() {}

    public static void sync(ServerPlayer player, DebuffSyncPacket.Anim anim, DebuffType type) {
        DebuffSyncPacket.sendTo(player, anim, type);
    }

    public static void syncFull(ServerPlayer player) {
        sync(player, DebuffSyncPacket.Anim.NONE, null);
    }

    /** 有活跃 debuff 时周期性刷新，避免客户端本地倒计时漂移。 */
    public static void heartbeatTick(ServerPlayer player) {
        if (!hasAnyDebuff(player)) {
            return;
        }
        if (player.level().getGameTime() % 40 != 0) {
            return;
        }
        syncFull(player);
    }

    public static boolean hasAnyDebuff(ServerPlayer player) {
        if (PlayerDebuffData.hasFracture(player)) {
            return true;
        }
        for (DebuffType type : DebuffType.values()) {
            if (type != DebuffType.FRACTURE && PlayerDebuffData.getStage(player, type) > 0) {
                return true;
            }
        }
        return false;
    }

    public static int computeFractureProgress(ServerPlayer player) {
        if (!PlayerDebuffData.hasFracture(player)) {
            return 0;
        }
        int max = PlayerDebuffData.getFractureMaxDuration(player);
        int dur = PlayerDebuffData.getFractureDuration(player);
        if (max <= 0 || dur <= 0) {
            return max > 0 && dur <= 0 ? 100 : 0;
        }
        int fill = DebuffConfig.fracture().progressFillFor(max);
        return Math.min(100, (max - dur) * 100 / fill);
    }

    public static int computeProgress(ServerPlayer player, DebuffType type) {
        if (type == DebuffType.FRACTURE) {
            return computeFractureProgress(player);
        }
        int stage = PlayerDebuffData.getStage(player, type);
        if (stage <= 0) {
            return 0;
        }
        DebuffConfig.DebuffRules rules = rules(type);
        int max = Math.max(1, rules.maxStage());
        int interval = scaledInterval(player, type, rules.stageIntervalTicks());
        int timer = PlayerDebuffData.getStageTimer(player, type);
        double base = (stage - 1.0) / max;
        double sub = 0;
        if (stage < max && interval > 0 && timer >= 0) {
            sub = (1.0 - timer / (double) interval) / max;
        }
        return Math.min(100, (int) ((base + sub) * 100));
    }

    private static int scaledInterval(ServerPlayer player, DebuffType type, int base) {
        double mult = DebuffModifiers.stageDelayMultiplier(player, type);
        if (type == DebuffType.BLEED) {
            mult *= PlayerDebuffData.getBleedIntervalMult(player);
        }
        return Math.max(1, (int) (base * mult));
    }

    private static DebuffConfig.DebuffRules rules(DebuffType type) {
        return switch (type) {
            case BLEED -> DebuffConfig.bleed();
            case FRACTURE -> DebuffConfig.fracture();
            case POISON -> DebuffConfig.poison();
            case INFECTION -> DebuffConfig.infection();
        };
    }
}
