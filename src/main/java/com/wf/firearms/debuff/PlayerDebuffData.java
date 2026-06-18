package com.wf.firearms.debuff;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public final class PlayerDebuffData {
    private static final String KEY = "debuffs";

    private PlayerDebuffData() {}

    private static CompoundTag debuffs(Player player) {
        CompoundTag root = com.wf.firearms.data.PlayerFirearmsData.root(player);
        if (!root.contains(KEY)) {
            root.put(KEY, new CompoundTag());
        }
        return root.getCompound(KEY);
    }

    public static int getStage(Player player, DebuffType type) {
        return debuffs(player).getInt(type.getId() + "_stage");
    }

    public static void setStage(Player player, DebuffType type, int stage) {
        debuffs(player).putInt(type.getId() + "_stage", Math.max(0, stage));
    }

    public static int getStageTimer(Player player, DebuffType type) {
        return debuffs(player).getInt(type.getId() + "_timer");
    }

    public static void setStageTimer(Player player, DebuffType type, int ticks) {
        debuffs(player).putInt(type.getId() + "_timer", Math.max(0, ticks));
    }

    public static int getDotTimer(Player player, DebuffType type) {
        return debuffs(player).getInt(type.getId() + "_dot");
    }

    public static void setDotTimer(Player player, DebuffType type, int ticks) {
        debuffs(player).putInt(type.getId() + "_dot", Math.max(0, ticks));
    }

    public static boolean hasFracture(Player player) {
        return debuffs(player).getBoolean("fracture_active");
    }

    public static void setFracture(Player player, boolean active, int durationTicks) {
        CompoundTag tag = debuffs(player);
        tag.putBoolean("fracture_active", active);
        if (active) {
            tag.putInt("fracture_duration", durationTicks);
            tag.putInt("fracture_max_duration", durationTicks);
            tag.putBoolean("fracture_bleed_escalated", false);
        } else {
            tag.remove("fracture_duration");
            tag.remove("fracture_max_duration");
            tag.remove("fracture_bleed_escalated");
        }
    }

    public static int getFractureMaxDuration(Player player) {
        int max = debuffs(player).getInt("fracture_max_duration");
        return max > 0 ? max : getFractureDuration(player);
    }

    public static boolean hasFractureBleedEscalated(Player player) {
        return debuffs(player).getBoolean("fracture_bleed_escalated");
    }

    public static void setFractureBleedEscalated(Player player, boolean escalated) {
        debuffs(player).putBoolean("fracture_bleed_escalated", escalated);
    }

    public static int getFractureDuration(Player player) {
        return debuffs(player).getInt("fracture_duration");
    }

    public static void setFractureDuration(Player player, int ticks) {
        debuffs(player).putInt("fracture_duration", Math.max(0, ticks));
    }

    public static void clearAll(Player player) {
        debuffs(player).getAllKeys().forEach(k -> debuffs(player).remove(k));
    }

    public static void clear(Player player, DebuffType type) {
        if (type == DebuffType.FRACTURE) {
            setFracture(player, false, 0);
            return;
        }
        setStage(player, type, 0);
        setStageTimer(player, type, 0);
        setDotTimer(player, type, 0);
    }

    /** 止血剂：暂停出血阶段推进的剩余 tick。 */
    public static int getBleedPauseTicks(Player player) {
        return debuffs(player).getInt("bleed_pause");
    }

    public static void setBleedPauseTicks(Player player, int ticks) {
        debuffs(player).putInt("bleed_pause", Math.max(0, ticks));
    }

    public static void tickBleedPause(Player player) {
        int t = getBleedPauseTicks(player);
        if (t > 0) {
            setBleedPauseTicks(player, t - 1);
        }
    }

    /** 出血恶化间隔额外倍率（与 {@link com.wf.firearms.debuff.DebuffModifiers} 叠乘）。 */
    public static double getBleedIntervalMult(Player player) {
        int left = debuffs(player).getInt("bleed_slow_ticks");
        if (left <= 0) {
            return 1.0;
        }
        return debuffs(player).getDouble("bleed_slow_mult");
    }

    public static void tickBleedSlow(Player player) {
        int t = debuffs(player).getInt("bleed_slow_ticks");
        if (t > 0) {
            debuffs(player).putInt("bleed_slow_ticks", t - 1);
        }
    }

    public static void setBleedProtection(Player player, int pauseTicks, double intervalMult) {
        CompoundTag tag = debuffs(player);
        int pause = Math.max(tag.getInt("bleed_pause"), pauseTicks);
        tag.putInt("bleed_pause", pause);
        tag.putInt("bleed_pause_max", Math.max(tag.getInt("bleed_pause_max"), pause));
        tag.putInt("bleed_slow_ticks", Math.max(tag.getInt("bleed_slow_ticks"), pauseTicks));
        tag.putDouble("bleed_slow_mult", Math.max(tag.getDouble("bleed_slow_mult"), intervalMult));
    }

    public static int getBleedPauseMax(Player player) {
        return debuffs(player).getInt("bleed_pause_max");
    }

    public static void tickBleedPauseMax(Player player) {
        if (getBleedPauseTicks(player) <= 0) {
            debuffs(player).remove("bleed_pause_max");
        }
    }
}
