package com.wf.firearms.debuff;

import com.wf.firearms.network.DebuffSyncPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public final class DebuffService {
    private static final Random RANDOM = new Random();

    private DebuffService() {}

    public static void tickPlayer(ServerPlayer player) {
        tickStaged(player, DebuffType.BLEED, DebuffConfig.bleed());
        tickStaged(player, DebuffType.POISON, DebuffConfig.poison());
        tickStaged(player, DebuffType.INFECTION, DebuffConfig.infection());
        tickFracture(player);
        trySpreadInfection(player);
        PlayerDebuffData.tickBleedPauseMax(player);
        DebuffSyncService.heartbeatTick(player);
    }

    private static void tickStaged(ServerPlayer player, DebuffType type, DebuffConfig.DebuffRules rules) {
        int stage = PlayerDebuffData.getStage(player, type);
        if (stage <= 0) {
            return;
        }
        if (type == DebuffType.BLEED) {
            PlayerDebuffData.tickBleedPause(player);
            PlayerDebuffData.tickBleedSlow(player);
        }
        int stageTimer = PlayerDebuffData.getStageTimer(player, type);
        boolean pauseWorsen = type == DebuffType.BLEED && PlayerDebuffData.getBleedPauseTicks(player) > 0;
        if (!pauseWorsen) {
            stageTimer--;
        }
        if (stageTimer <= 0 && stage < rules.maxStage()) {
            stage++;
            PlayerDebuffData.setStage(player, type, stage);
            stageTimer = scaledInterval(player, type, rules.stageIntervalTicks());
            DebuffNotify.onStageTimerWorsen(player, type, stage);
            DebuffSyncService.sync(player, DebuffSyncPacket.Anim.WORSE, type);
        } else if (stageTimer <= 0) {
            stageTimer = scaledInterval(player, type, rules.stageIntervalTicks());
        }
        PlayerDebuffData.setStageTimer(player, type, stageTimer);

        int dotTimer = PlayerDebuffData.getDotTimer(player, type) - 1;
        if (dotTimer <= 0) {
            float dmg = computeDotDamage(player, type, stage, rules);
            if (dmg > 0.01f) {
                player.hurt(ModDamageSources.forDebuff(player.level(), type), dmg);
            }
            PlayerDebuffData.setDotTimer(player, type, rules.dotIntervalForStage(stage));
        } else {
            PlayerDebuffData.setDotTimer(player, type, dotTimer);
        }
    }

    /** 出血：每阶段 N 点固定伤害 + N% 最大生命值；其它 debuff 仍用 dot_per_stage。 */
    private static float computeDotDamage(
            ServerPlayer player, DebuffType type, int stage, DebuffConfig.DebuffRules rules) {
        double mult = DebuffModifiers.damageTakenMultiplier(player);
        if (type == DebuffType.BLEED
                && (rules.dotFlatPerStage() > 0 || rules.dotMaxHealthPercentPerStage() > 0)) {
            double flat = rules.dotFlatPerStage() * stage;
            double percent = player.getMaxHealth() * rules.dotMaxHealthPercentPerStage() * stage;
            return (float) ((flat + percent) * mult);
        }
        return (float) (rules.dotPerStage() * stage * mult);
    }

    private static void tickFracture(ServerPlayer player) {
        if (!PlayerDebuffData.hasFracture(player)) {
            return;
        }
        var rules = DebuffConfig.fracture();
        int dur = PlayerDebuffData.getFractureDuration(player) - 1;
        if (dur <= 0) {
            PlayerDebuffData.setFracture(player, false, 0);
            FractureModifiers.clear(player);
            player.displayClientMessage(Component.translatable("gunsrpg.debuff.fracture_healed"), true);
            DebuffSyncService.sync(player, DebuffSyncPacket.Anim.CURE, DebuffType.FRACTURE);
            return;
        }
        PlayerDebuffData.setFractureDuration(player, dur);
        FractureModifiers.sync(player);
        tryFractureBleedEscalation(player);
        int fatigue = rules.miningFatigueAmplifier();
        if (fatigue >= 0) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, fatigue, false, false, true));
        }
        if (player.isSprinting() && rules.sprintDamage() > 0) {
            player.hurt(ModDamageSources.fracture(player.level()), rules.sprintDamage());
        }
    }

    public static void onPlayerJump(ServerPlayer player) {
        if (!PlayerDebuffData.hasFracture(player)) {
            return;
        }
        float dmg = DebuffConfig.fracture().jumpDamage();
        if (dmg > 0) {
            player.hurt(ModDamageSources.fracture(player.level()), dmg);
        }
    }

    private static void trySpreadInfection(ServerPlayer player) {
        var rules = DebuffConfig.infection();
        int bleed = PlayerDebuffData.getStage(player, DebuffType.BLEED);
        if (bleed < rules.bleedStageRequire()) {
            return;
        }
        if (PlayerDebuffData.getStage(player, DebuffType.INFECTION) > 0) {
            return;
        }
        if (RANDOM.nextDouble() < rules.spreadChance() * (1.0 - DebuffModifiers.procResist(player, DebuffType.INFECTION))) {
            applyStage(player, DebuffType.INFECTION, 1, DebuffCause.INFECTION);
        }
    }

    private static void tryFractureBleedEscalation(ServerPlayer player) {
        if (PlayerDebuffData.hasFractureBleedEscalated(player)) {
            return;
        }
        int max = PlayerDebuffData.getFractureMaxDuration(player);
        int dur = PlayerDebuffData.getFractureDuration(player);
        if (dur <= 0 || max <= 0) {
            return;
        }
        int fill = DebuffConfig.fracture().progressFillFor(max);
        int progress = Math.min(100, (max - dur) * 100 / fill);
        if (progress < 100) {
            return;
        }
        PlayerDebuffData.setFractureBleedEscalated(player, true);
        int bleed = PlayerDebuffData.getStage(player, DebuffType.BLEED);
        int bleedMax = DebuffConfig.bleed().maxStage();
        if (bleed <= 0) {
            applyStage(player, DebuffType.BLEED, 1, DebuffCause.FRACTURE, true);
            DebuffNotify.onFractureBleedEscalation(player, 1);
            DebuffSyncService.sync(player, DebuffSyncPacket.Anim.WORSE, DebuffType.BLEED);
            return;
        }
        if (bleed >= bleedMax) {
            return;
        }
        int next = bleed + 1;
        resetBleedStageProgress(player, next);
        DebuffNotify.onFractureBleedEscalation(player, next);
        DebuffSyncService.sync(player, DebuffSyncPacket.Anim.WORSE, DebuffType.BLEED);
    }

    private static void resetBleedStageProgress(ServerPlayer player, int stage) {
        var rules = DebuffConfig.bleed();
        PlayerDebuffData.setStage(player, DebuffType.BLEED, stage);
        PlayerDebuffData.setStageTimer(
                player, DebuffType.BLEED, scaledInterval(player, DebuffType.BLEED, rules.stageIntervalTicks()));
        PlayerDebuffData.setDotTimer(player, DebuffType.BLEED, rules.dotIntervalForStage(stage));
    }

    public static void onHurt(ServerPlayer player, float amount, boolean fall) {
        if (fall) {
            tryFractureFromFall(player, amount);
        } else if (amount > 0.5f) {
            tryProc(player, DebuffType.BLEED, DebuffConfig.bleed().hurtProcChance(), DebuffCause.HURT);
            tryProc(player, DebuffType.POISON, DebuffConfig.poison().hurtProcChance(), DebuffCause.HURT);
            if (!PlayerDebuffData.hasFracture(player)) {
                tryProc(player, DebuffType.FRACTURE, DebuffConfig.fracture().hurtProcChance(), DebuffCause.HURT);
            }
        }
    }

    private static void tryFractureFromFall(ServerPlayer player, float amount) {
        var rules = DebuffConfig.fracture();
        if (amount < rules.fallMinDamage() || PlayerDebuffData.hasFracture(player)) {
            return;
        }
        double chance = rules.fallProcChance() * (1.0 - DebuffModifiers.procResist(player, DebuffType.FRACTURE));
        if (RANDOM.nextDouble() < chance) {
            applyFracture(player, DebuffCause.FALL);
        }
    }

    private static void tryProc(ServerPlayer player, DebuffType type, double baseChance, DebuffCause cause) {
        if (baseChance <= 0) {
            return;
        }
        if (type != DebuffType.FRACTURE && PlayerDebuffData.getStage(player, type) >= rules(type).maxStage()) {
            return;
        }
        double chance = baseChance * (1.0 - DebuffModifiers.procResist(player, type));
        if (RANDOM.nextDouble() < chance) {
            if (type == DebuffType.FRACTURE) {
                applyFracture(player, cause);
            } else {
                int next = Math.min(rules(type).maxStage(), PlayerDebuffData.getStage(player, type) + 1);
                applyStage(player, type, next, cause);
            }
        }
    }

    public static void applyFracture(ServerPlayer player) {
        applyFracture(player, DebuffCause.HURT);
    }

    public static void applyFracture(ServerPlayer player, DebuffCause cause) {
        applyFracture(player, cause, false);
    }

    public static void applyFracture(ServerPlayer player, DebuffCause cause, boolean silent) {
        var rules = DebuffConfig.fracture();
        PlayerDebuffData.setFracture(player, true, rules.durationTicks());
        FractureModifiers.sync(player);
        if (!silent) {
            DebuffNotify.onFractureApplied(player, cause);
        }
        DebuffSyncService.sync(player, DebuffSyncPacket.Anim.APPLY, DebuffType.FRACTURE);
    }

    public static void applyStage(ServerPlayer player, DebuffType type, int stage) {
        applyStage(player, type, stage, DebuffCause.UNKNOWN);
    }

    public static void applyStage(ServerPlayer player, DebuffType type, int stage, DebuffCause cause) {
        applyStage(player, type, stage, cause, false);
    }

    public static void applyStage(
            ServerPlayer player, DebuffType type, int stage, DebuffCause cause, boolean silent) {
        int prev = PlayerDebuffData.getStage(player, type);
        PlayerDebuffData.setStage(player, type, stage);
        PlayerDebuffData.setStageTimer(player, type, scaledInterval(player, type, rules(type).stageIntervalTicks()));
        PlayerDebuffData.setDotTimer(player, type, rules(type).dotIntervalForStage(stage));
        if (!silent) {
            DebuffNotify.onApplied(player, type, cause, stage, prev);
        }
        DebuffSyncPacket.Anim anim = prev <= 0 ? DebuffSyncPacket.Anim.APPLY
                : (stage > prev ? DebuffSyncPacket.Anim.WORSE : DebuffSyncPacket.Anim.HEAL);
        DebuffSyncService.sync(player, anim, type);
    }

    private static int scaledInterval(Player player, DebuffType type, int base) {
        double mult = DebuffModifiers.stageDelayMultiplier(player, type);
        if (type == DebuffType.BLEED) {
            mult *= PlayerDebuffData.getBleedIntervalMult(player);
        }
        return (int) (base * mult);
    }

    private static DebuffConfig.DebuffRules rules(DebuffType type) {
        return switch (type) {
            case BLEED -> DebuffConfig.bleed();
            case FRACTURE -> DebuffConfig.fracture();
            case POISON -> DebuffConfig.poison();
            case INFECTION -> DebuffConfig.infection();
        };
    }

    /** 阶段型 debuff 降 1 级；骨折等同完全治愈。 */
    public static boolean tryReduce(ServerPlayer player, DebuffType type) {
        if (type == DebuffType.FRACTURE) {
            return tryCure(player, type);
        }
        int stage = PlayerDebuffData.getStage(player, type);
        if (stage <= 0) {
            return false;
        }
        if (stage <= 1) {
            PlayerDebuffData.clear(player, type);
        } else {
            PlayerDebuffData.setStage(player, type, stage - 1);
            PlayerDebuffData.setStageTimer(player, type, scaledInterval(player, type, rules(type).stageIntervalTicks()));
            PlayerDebuffData.setDotTimer(player, type, rules(type).dotIntervalForStage(stage - 1));
        }
        DebuffSyncService.sync(player, DebuffSyncPacket.Anim.HEAL, type);
        return true;
    }

    /** 若玩家带有该 debuff 则清除；返回是否实际治愈 */
    public static boolean tryCure(ServerPlayer player, DebuffType type) {
        if (type == DebuffType.FRACTURE) {
            if (!PlayerDebuffData.hasFracture(player)) {
                return false;
            }
            PlayerDebuffData.clear(player, type);
            FractureModifiers.clear(player);
            DebuffSyncService.sync(player, DebuffSyncPacket.Anim.CURE, type);
            return true;
        }
        if (PlayerDebuffData.getStage(player, type) > 0) {
            PlayerDebuffData.clear(player, type);
            DebuffSyncService.sync(player, DebuffSyncPacket.Anim.CURE, type);
            return true;
        }
        return false;
    }

    public static void clearAll(ServerPlayer player) {
        for (DebuffType type : DebuffType.values()) {
            PlayerDebuffData.clear(player, type);
        }
        FractureModifiers.clear(player);
        player.displayClientMessage(Component.translatable("gunsrpg.debuff.cleared"), true);
        DebuffSyncService.syncFull(player);
    }

    public static String statusLine(Player player) {
        StringBuilder sb = new StringBuilder();
        for (DebuffType type : DebuffType.values()) {
            if (type == DebuffType.FRACTURE) {
                if (PlayerDebuffData.hasFracture(player)) {
                    append(sb, type.displayName().getString());
                }
                continue;
            }
            int stage = PlayerDebuffData.getStage(player, type);
            if (stage > 0) {
                append(sb, type.displayName().getString() + " " + stage);
            }
        }
        return sb.isEmpty() ? "无" : sb.toString();
    }

    private static void append(StringBuilder sb, String part) {
        if (!sb.isEmpty()) {
            sb.append(" · ");
        }
        sb.append(part);
    }
}
