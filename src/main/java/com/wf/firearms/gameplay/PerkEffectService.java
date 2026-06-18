package com.wf.firearms.gameplay;

import com.wf.firearms.combat.WeaponClass;
import com.wf.firearms.data.PerkDef;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.SkillDatabase;
import net.minecraft.world.entity.player.Player;

/**
 * 天赋数值统一入口（52 项 Perk 均经 {@link PlayerFirearmsData#getPerkMultiplier} 读取步数）。
 */
public final class PerkEffectService {
    private PerkEffectService() {}

    public static double multiplier(Player player, String perkId) {
        return PlayerFirearmsData.getPerkMultiplier(player, perkId);
    }

    /** 医疗品治愈强度：1 + 对应 effect 天赋（正投资增强）。 */
    public static double medicalPotency(Player player, String effectPerkId) {
        return 1.0 + positiveOnly(player, effectPerkId);
    }

    /** 额外治愈层数（0~2），高投资绷带等可多减 1 层。 */
    public static int extraCureLayers(Player player, String effectPerkId) {
        double v = positiveOnly(player, effectPerkId);
        if (v >= 0.12) {
            return 2;
        }
        if (v >= 0.05) {
            return 1;
        }
        return 0;
    }

    /** CGM / 原生弹：高位瞄准全身伤害倍率（TaCZ 模式请用 {@link #taczHeadshotMultiplierFactor}）。 */
    public static double headshotDamageMultiplier(Player player) {
        return 1.0 + positiveOnly(player, "headshot_damage");
    }

    /** TaCZ 爆头：在枪身 headshot 倍率上，天赋每投资 1 级 +25%。 */
    public static float taczHeadshotMultiplierFactor(Player player) {
        int steps = PlayerFirearmsData.getPerkInvestment(player, "headshot_damage");
        return 1.0f + 0.25f * Math.max(0, steps);
    }

    public static double meleeDamageMultiplier(Player player) {
        return 1.0 + positiveOnly(player, "melee_damage");
    }

    public static double reloadSpeedMultiplier(Player player) {
        double mult = Math.max(0.5, 1.0 - positiveOnly(player, "reload_speed"));
        mult *= SurvivalSkillService.adrenalineReloadMultiplier(player);
        return mult;
    }

    public static double recoilMultiplier(Player player) {
        return Math.max(0.4, 1.0 - positiveOnly(player, "recoil_control"));
    }

    public static double jamChanceMultiplier(Player player) {
        return jamChanceMultiplier(player, null);
    }

    /** @param weaponKey 当前 wf 枪械 key；用于「坚实可靠」等枪专属减卡弹。 */
    public static double jamChanceMultiplier(Player player, String weaponKey) {
        double mult = Math.max(0.0, 1.0 - positiveOnly(player, "weapon_jamming"));
        mult *= carefulGunnerJamMultiplier(player);
        if (weaponKey != null && !weaponKey.isEmpty()) {
            String reliableSkill = weaponKey + "_reliable";
            if (PlayerFirearmsData.isUnlocked(player, reliableSkill)) {
                mult *= 0.8;
            }
        }
        return mult;
    }

    public static double unjammingSpeedMultiplier(Player player) {
        double mult = Math.max(0.2, 1.0 - positiveOnly(player, "unjamming_speed"));
        double timeMult = carefulGunnerUnjamTimeMultiplier(player);
        if (timeMult < 1.0) {
            mult /= timeMult;
        }
        return mult;
    }

    /** 射击损耗概率倍率（1.0 = 每发必耗 1 点；幸运枪手/天赋/坚实可靠降低）。 */
    public static double wearDamageMultiplier(Player player, String weaponKey) {
        double mult = Math.max(0.05, 1.0 - positiveOnly(player, "weapon_durability"));
        mult *= luckyShooterWearMultiplier(player);
        if (weaponKey != null && !weaponKey.isEmpty()) {
            String reliableSkill = weaponKey + "_reliable";
            if (PlayerFirearmsData.isUnlocked(player, reliableSkill)) {
                mult *= 0.85;
            }
        }
        return mult;
    }

    /** 细心枪手 I–V：卡弹 ×0.90 / 0.75 / 0.55 / 0.30 / 0（仅最高级）。 */
    private static double carefulGunnerJamMultiplier(Player player) {
        return switch (carefulGunnerTier(player)) {
            case 5 -> 0.0;
            case 4 -> 0.30;
            case 3 -> 0.55;
            case 2 -> 0.75;
            case 1 -> 0.90;
            default -> 1.0;
        };
    }

    /** 细心枪手：排障时间 ×0.95 / 0.90 / 0.85 / 0.75 / 0.65。 */
    private static double carefulGunnerUnjamTimeMultiplier(Player player) {
        return switch (carefulGunnerTier(player)) {
            case 5 -> 0.65;
            case 4 -> 0.75;
            case 3 -> 0.85;
            case 2 -> 0.90;
            case 1 -> 0.95;
            default -> 1.0;
        };
    }

    /** 幸运枪手 I–V：射击损耗 ×0.95 / 0.88 / 0.78 / 0.62 / 0.45（V 仍保留 45% 损耗）。 */
    private static double luckyShooterWearMultiplier(Player player) {
        return switch (luckyShooterTier(player)) {
            case 5 -> 0.45;
            case 4 -> 0.62;
            case 3 -> 0.78;
            case 2 -> 0.88;
            case 1 -> 0.95;
            default -> 1.0;
        };
    }

    private static int carefulGunnerTier(Player player) {
        if (PlayerFirearmsData.isUnlocked(player, "careful_gunner_v")) {
            return 5;
        }
        if (PlayerFirearmsData.isUnlocked(player, "careful_gunner_iv")) {
            return 4;
        }
        if (PlayerFirearmsData.isUnlocked(player, "careful_gunner_iii")) {
            return 3;
        }
        if (PlayerFirearmsData.isUnlocked(player, "careful_gunner_ii")) {
            return 2;
        }
        if (PlayerFirearmsData.isUnlocked(player, "careful_gunner_i")) {
            return 1;
        }
        return 0;
    }

    private static int luckyShooterTier(Player player) {
        if (PlayerFirearmsData.isUnlocked(player, "lucky_shooter_v")) {
            return 5;
        }
        if (PlayerFirearmsData.isUnlocked(player, "lucky_shooter_iv")) {
            return 4;
        }
        if (PlayerFirearmsData.isUnlocked(player, "lucky_shooter_iii")) {
            return 3;
        }
        if (PlayerFirearmsData.isUnlocked(player, "lucky_shooter_ii")) {
            return 2;
        }
        if (PlayerFirearmsData.isUnlocked(player, "lucky_shooter_i")) {
            return 1;
        }
        return 0;
    }

    public static double motherlodeMultiplier(Player player) {
        return 1.0 + positiveOnly(player, "motherlode_bonus");
    }

    /** 本 mod 枪械伤害倍率（按枪族叠加对应 Perk）。 */
    public static double firearmDamageMultiplier(Player player, WeaponClass weaponClass) {
        double mult = 1.0;
        mult += positiveOnly(player, "pistol_damage") * match(weaponClass, WeaponClass.PISTOL);
        mult += positiveOnly(player, "smg_damage") * match(weaponClass, WeaponClass.SMG);
        mult += positiveOnly(player, "ar_damage") * match(weaponClass, WeaponClass.RIFLE);
        mult += positiveOnly(player, "dmr_damage") * match(weaponClass, WeaponClass.DMR);
        mult += positiveOnly(player, "sr_damage") * match(weaponClass, WeaponClass.SNIPER);
        mult += positiveOnly(player, "shotgun_damage") * match(weaponClass, WeaponClass.SHOTGUN);
        mult += positiveOnly(player, "loud_weapon_damage") * match(weaponClass, WeaponClass.HEAVY);
        return Math.max(0.1, mult);
    }

    private static double match(WeaponClass a, WeaponClass b) {
        return a == b ? 1.0 : 0.0;
    }

    public static double foodDurationMultiplier(Player player) {
        return 1.0 + positiveOnly(player, "well_fed_duration");
    }

    /** 消音/高噪武器伤害天赋（对齐原版 GunItem#getWeaponDamage）。 */
    public static double noiseCategoryDamageMultiplier(Player player, boolean silenced) {
        if (silenced) {
            return 1.0 + positiveOnly(player, "silent_weapon_damage");
        }
        return 1.0 + positiveOnly(player, "loud_weapon_damage");
    }

    /** 枪声倍率（消音器 × 天赋；值越小越安静）。 */
    public static double weaponNoiseMultiplier(Player player, String weaponKey, boolean silenced) {
        double base = silenced ? 0.2 : 1.0;
        double perk = Math.max(0.05, 1.0 - positiveOnly(player, "weapon_noise"));
        return base * perk;
    }

    public static double bowDamageMultiplier(Player player) {
        return 1.0 + positiveOnly(player, "bow_damage");
    }

    public static double meleeCooldownAttackSpeedBonus(Player player) {
        return positiveOnly(player, "melee_cooldown");
    }

    /** 紧急空投（god_help_us）冷却缩短比例上限（最多 −80%，即冷却 ×0.2）。 */
    public static final double EMERGENCY_AIRDROP_COOLDOWN_REDUCTION_CAP = 0.8;

    /** 应用天赋后的紧急空投冷却 tick（基础 7 游戏日）。 */
    public static long emergencyAirdropCooldownTicks(Player player) {
        long base = 7L * 24000L;
        double reduction =
                Math.min(EMERGENCY_AIRDROP_COOLDOWN_REDUCTION_CAP, positiveOnly(player, "airdrop_call_cooldown"));
        return Math.max(24000L, Math.round(base * (1.0 - reduction)));
    }

    /** KubeJS / 外部脚本读取。 */
    public static double exportMultiplier(Player player, String perkId) {
        return multiplier(player, perkId);
    }

    private static double positiveOnly(Player player, String perkId) {
        return SkillDatabase.getPerk(perkId)
                .map(perk -> rawPositive(player, perk))
                .orElse(0.0);
    }

    private static double rawPositive(Player player, PerkDef perk) {
        int steps = PlayerFirearmsData.getPerkInvestment(player, perk.getId());
        double raw = steps * perk.getScaling();
        if (perk.isInvertCalculation()) {
            return Math.max(0, Math.min(perk.getBuffBound(), raw));
        }
        return Math.max(0, Math.min(perk.getBuffBound(), raw));
    }
}
