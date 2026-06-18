package com.wf.firearms.gameplay;

import com.wf.firearms.data.PlayerFirearmsData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

/** 采矿/伐木技能数值（对齐原版 Guns RPG Modifiers / LumberjackSkill / MotherlodeSkill）。 */
public final class MiningSkillService {
    private static final float[][] LUMBERJACK = {
            {0.10F, 0.20F},
            {0.25F, 0.40F},
            {0.40F, 0.60F},
            {0.55F, 0.80F},
            {0.70F, 0.95F}
    };

    private static final float[][] MOTHER_LODE = {
            {0.10F, 0.00F},
            {0.20F, 0.00F},
            {0.35F, 0.00F},
            {0.50F, 0.15F},
            {0.65F, 0.25F}
    };

    private MiningSkillService() {}

    public static int lumberjackTier(Player player) {
        return romanTier(player, "lumberjack");
    }

    public static float lumberjackPlankChance(Player player) {
        return lumberjackPlankChanceForTier(lumberjackTier(player));
    }

    public static float lumberjackStickChance(Player player) {
        return lumberjackStickChanceForTier(lumberjackTier(player));
    }

    public static float lumberjackPlankChanceForTier(int tier) {
        return tier > 0 ? LUMBERJACK[Math.min(tier - 1, LUMBERJACK.length - 1)][0] : 0.0F;
    }

    public static float lumberjackStickChanceForTier(int tier) {
        return tier > 0 ? LUMBERJACK[Math.min(tier - 1, LUMBERJACK.length - 1)][1] : 0.0F;
    }

    public static int motherLodeTier(Player player) {
        return romanTier(player, "mother_lode");
    }

    public static int graveDiggerTier(Player player) {
        return romanTier(player, "grave_digger");
    }

    public static int heavyPickaxeTier(Player player) {
        return romanTier(player, "heavy_pickaxe");
    }

    public static int sharpAxeTier(Player player) {
        return romanTier(player, "sharp_axe");
    }

    public static int acrobaticsTier(Player player) {
        if (PlayerFirearmsData.isUnlocked(player, "acrobatics_iii")) {
            return 3;
        }
        if (PlayerFirearmsData.isUnlocked(player, "acrobatics_ii")) {
            return 2;
        }
        if (PlayerFirearmsData.isUnlocked(player, "acrobatics_i")) {
            return 1;
        }
        return 0;
    }

    /** 原版 CHOPPING / MINING / DIGGING 倍率（MULB）。 */
    public static float sharpAxeSpeedBonus(int tier) {
        return tier * 0.2F;
    }

    public static float heavyPickaxeSpeedBonus(int tier) {
        return tier * 0.2F;
    }

    public static float graveDiggerSpeedBonus(int tier) {
        return switch (tier) {
            case 5 -> 0.80F;
            case 4 -> 0.60F;
            case 3 -> 0.45F;
            case 2 -> 0.30F;
            case 1 -> 0.15F;
            default -> 0.0F;
        };
    }

    public static int motherLodeDropMultiplier(Player player, RandomSource random) {
        int tier = motherLodeTier(player);
        if (tier <= 0) {
            return 1;
        }
        float[] row = MOTHER_LODE[Math.min(tier - 1, MOTHER_LODE.length - 1)];
        float doubleDrop = row[0];
        float tripleDrop = row[1];
        float f = random.nextFloat();
        if (f < tripleDrop) {
            return 3;
        }
        if ((f - tripleDrop) < doubleDrop) {
            return 2;
        }
        return 1;
    }

    public static int gunpowderYield(Player player) {
        if (PlayerFirearmsData.isUnlocked(player, "gunpowder_master")) {
            return 6;
        }
        if (PlayerFirearmsData.isUnlocked(player, "gunpowder_expert")) {
            return 4;
        }
        if (PlayerFirearmsData.isUnlocked(player, "gunpowder_novice")) {
            return 2;
        }
        return 0;
    }

    private static int romanTier(Player player, String prefix) {
        if (PlayerFirearmsData.isUnlocked(player, prefix + "_v")) {
            return 5;
        }
        if (PlayerFirearmsData.isUnlocked(player, prefix + "_iv")) {
            return 4;
        }
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
}
