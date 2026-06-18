package com.wf.firearms.debuff;

import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.SkillDatabase;
import net.minecraft.world.entity.player.Player;

/**
 * 合并天赋 Perk 与技能树抗性节点（bleeding_resistance_i 等）对 debuff 的影响。
 */
public final class DebuffModifiers {
    private DebuffModifiers() {}

    /** 减少触发几率，0~0.85 */
    public static double procResist(Player player, DebuffType type) {
        double skill = skillResist(player, type);
        double perk = perkResist(player, type);
        return Math.min(0.85, skill + perk);
    }

    /** 阶段推进间隔倍率，越大越慢恶化 */
    public static double stageDelayMultiplier(Player player, DebuffType type) {
        double skillTicks = skillDelayTicks(player, type);
        double perkDelay = perkDelay(player, type);
        return 1.0 + skillTicks / 600.0 + perkDelay;
    }

    public static double damageTakenMultiplier(Player player) {
        double reduction = PlayerFirearmsData.getPerkMultiplier(player, "damage_taken");
        return Math.max(0.0, 1.0 - reduction);
    }

    private static double skillResist(Player player, DebuffType type) {
        String prefix = switch (type) {
            case BLEED -> "bleeding_resistance";
            case FRACTURE -> "fracture_resistance";
            case POISON -> "poison_resistance";
            case INFECTION -> "infection_resistance";
        };
        int tier = resistanceTier(player, prefix);
        if (tier <= 0) {
            return 0;
        }
        double[] table = rules(type).skillResist();
        return table[Math.min(tier - 1, table.length - 1)];
    }

    private static int skillDelayTicks(Player player, DebuffType type) {
        String prefix = switch (type) {
            case BLEED -> "bleeding_resistance";
            case FRACTURE -> "fracture_resistance";
            case POISON -> "poison_resistance";
            case INFECTION -> "infection_resistance";
        };
        int tier = resistanceTier(player, prefix);
        if (tier <= 0) {
            return 0;
        }
        int[] table = rules(type).skillDelayTicks();
        return table[Math.min(tier - 1, table.length - 1)];
    }

    private static int resistanceTier(Player player, String prefix) {
        if (PlayerFirearmsData.isUnlocked(player, prefix + "_iii")) {
            return 3;
        }
        if (PlayerFirearmsData.isUnlocked(player, prefix + "_ii")) {
            return 2;
        }
        if (PlayerFirearmsData.isUnlocked(player, prefix + "_i")) {
            return 1;
        }
        return 0;
    }

    private static double perkResist(Player player, DebuffType type) {
        String perkId = switch (type) {
            case BLEED -> "bleed_resistance";
            case FRACTURE -> "fracture_resistance";
            case POISON -> "poison_resistance";
            case INFECTION -> "infection_resistance";
        };
        return positivePerkValue(player, perkId);
    }

    private static double perkDelay(Player player, DebuffType type) {
        String perkId = switch (type) {
            case BLEED -> "bleed_delay";
            case FRACTURE -> "fracture_delay";
            case POISON -> "poison_delay";
            case INFECTION -> "infection_delay";
        };
        return positivePerkValue(player, perkId);
    }

    /** 抗性类 Perk：正投资 = 增益（invertCalculation） */
    private static double positivePerkValue(Player player, String perkId) {
        return SkillDatabase.getPerk(perkId)
                .map(perk -> {
                    int steps = PlayerFirearmsData.getPerkInvestment(player, perkId);
                    double raw = steps * perk.getScaling();
                    if (perk.isInvertCalculation()) {
                        return Math.max(0, Math.min(perk.getBuffBound(), raw));
                    }
                    return Math.max(-perk.getDebuffBound(), Math.min(perk.getBuffBound(), raw));
                })
                .orElse(0.0);
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
