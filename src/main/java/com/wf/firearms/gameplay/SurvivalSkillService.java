package com.wf.firearms.gameplay;

import com.wf.firearms.data.PlayerFirearmsData;
import net.minecraft.world.entity.player.Player;

/** 生存栏技能数值（对齐原版 Guns RPG，部分机制按整合包需求调整）。 */
public final class SurvivalSkillService {
    private static final String[] ROMAN_SUFFIX = {"", "_i", "_ii", "_iii", "_iv", "_v"};

    private SurvivalSkillService() {}

    public static int romanTier(Player player, String prefix) {
        for (int tier = 5; tier >= 1; tier--) {
            if (PlayerFirearmsData.isUnlocked(player, prefix + ROMAN_SUFFIX[tier])) {
                return tier;
            }
        }
        return 0;
    }

    public static int chefTier(Player player) {
        if (PlayerFirearmsData.isUnlocked(player, "executive_chef_v")) {
            return 5;
        }
        if (PlayerFirearmsData.isUnlocked(player, "head_chef_iv")) {
            return 4;
        }
        if (PlayerFirearmsData.isUnlocked(player, "sous_chef_iii")) {
            return 3;
        }
        if (PlayerFirearmsData.isUnlocked(player, "master_chef")) {
            return 2;
        }
        if (PlayerFirearmsData.isUnlocked(player, "local_chef")) {
            return 1;
        }
        return 0;
    }

    /** 肌肉猛男：原版为固定加值（非百分比），与神化等乘算类加成独立叠加。 */
    public static float strongMusclesBonus(Player player) {
        return switch (romanTier(player, "strong_muscles")) {
            case 5 -> 10.0F;
            case 4 -> 8.0F;
            case 3 -> 6.0F;
            case 2 -> 4.0F;
            case 1 -> 2.0F;
            default -> 0.0F;
        };
    }

    /** 碎颅者：固定 5% 触发，暴击倍率 = 1 + tier（I=2x … V=6x）。 */
    public static float skullCrusherCritMultiplier(Player player) {
        int tier = romanTier(player, "skull_crusher");
        return tier > 0 ? tier + 1.0F : 1.0F;
    }

    public static boolean rollSkullCrusher(Player player) {
        return romanTier(player, "skull_crusher") > 0 && player.getRandom().nextFloat() < 0.05F;
    }

    public static float wellFedChance(Player player) {
        return switch (romanTier(player, "well_fed")) {
            case 5 -> 0.90F;
            case 4 -> 0.80F;
            case 3 -> 0.70F;
            case 2 -> 0.55F;
            case 1 -> 0.40F;
            default -> 0.0F;
        };
    }

    public static int wellFedAbsorptionAmplifier(Player player) {
        return Math.max(0, romanTier(player, "well_fed") - 1);
    }

    public static int wellFedAbsorptionDurationTicks(Player player) {
        int tier = romanTier(player, "well_fed");
        if (tier <= 0) {
            return 0;
        }
        int base = 1200 + wellFedAbsorptionAmplifier(player) * 600;
        return (int) Math.round(base * PerkEffectService.foodDurationMultiplier(player));
    }

    /** 大量进食触发阈值（原版默认 8 饱食度）。 */
    public static int wellFedNutritionThreshold() {
        return 8;
    }

    public static int adrenalineTier(Player player) {
        return romanTier(player, "adrenaline_rush");
    }

    /** 低血量触发阈值：当前生命 ≤ 最大生命的该比例时生效。 */
    public static float adrenalineHealthThresholdRatio() {
        return 0.25F;
    }

    public static boolean adrenalineActive(Player player) {
        if (adrenalineTier(player) <= 0) {
            return false;
        }
        float max = player.getMaxHealth();
        if (max <= 0.0F) {
            return false;
        }
        return player.getHealth() <= max * adrenalineHealthThresholdRatio();
    }

    /** 低血量攻速加成（显示为百分比）。 */
    public static float adrenalineAttackSpeedBonus(Player player) {
        return switch (adrenalineTier(player)) {
            case 5 -> 0.65F;
            case 4 -> 0.55F;
            case 3 -> 0.50F;
            case 2 -> 0.30F;
            case 1 -> 0.15F;
            default -> 0.0F;
        };
    }

    /** 装填速度倍率（>1 更快）。 */
    public static double adrenalineReloadMultiplier(Player player) {
        if (!adrenalineActive(player)) {
            return 1.0;
        }
        return switch (adrenalineTier(player)) {
            case 5 -> 1.35;
            case 4 -> 1.28;
            case 3 -> 1.20;
            case 2 -> 1.10;
            case 1 -> 1.05;
            default -> 1.0;
        };
    }

    public static float agilityMoveSpeedBonus(Player player) {
        return switch (romanTier(player, "agility")) {
            case 5 -> 0.35F;
            case 4 -> 0.28F;
            case 3 -> 0.20F;
            case 2 -> 0.10F;
            case 1 -> 0.05F;
            default -> 0.0F;
        };
    }

    public static int secondChanceTier(Player player) {
        return romanTier(player, "second_chance");
    }

    public static float secondChanceHeal(Player player) {
        return switch (secondChanceTier(player)) {
            case 5 -> 30.0F;
            case 4 -> 25.0F;
            case 3 -> 20.0F;
            case 2 -> 15.0F;
            case 1 -> 10.0F;
            default -> 0.0F;
        };
    }

    public static int secondChanceCooldownTicks(Player player) {
        int minutes =
                switch (secondChanceTier(player)) {
                    case 5 -> 6;
                    case 4 -> 7;
                    case 3 -> 9;
                    case 2 -> 12;
                    case 1 -> 15;
                    default -> 0;
                };
        return minutes * 60 * 20;
    }

    public static int getSecondChanceCooldown(Player player) {
        return PlayerFirearmsData.root(player).getInt("second_chance_cd");
    }

    public static void setSecondChanceCooldown(Player player, int ticks) {
        PlayerFirearmsData.root(player).putInt("second_chance_cd", Math.max(0, ticks));
    }

    public static void tickSecondChanceCooldown(Player player) {
        int cd = getSecondChanceCooldown(player);
        if (cd > 0) {
            setSecondChanceCooldown(player, cd - 1);
        }
    }
}
