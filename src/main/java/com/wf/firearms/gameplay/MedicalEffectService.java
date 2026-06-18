package com.wf.firearms.gameplay;

import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.debuff.DebuffType;
import com.wf.firearms.debuff.PlayerDebuffData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

/**
 * 医疗品数值：按已损失生命百分比回血、Buff 叠级/续期、止血剂延缓出血、专业医师额外治疗。
 */
public final class MedicalEffectService {
    /** 基础止血剂持续时间（秒） */
    public static final int HEMOSTAT_BASE_SECONDS = 60;

    private MedicalEffectService() {}

    /** 已损失生命值比例（0~1） */
    public static float missingHealthFraction(Player player) {
        float max = player.getMaxHealth();
        if (max <= 0f) {
            return 0f;
        }
        return Math.max(0f, (max - player.getHealth()) / max);
    }

    /**
     * 读条结束后统一结算：固定回血、已损失生命百分比、可选 Buff，最后结算「专业医师」。
     */
    public static void applyAfterMedUse(
            Player player, float flatHeal, double missingHealthPercent, String effectPerkId, Runnable applyBuffs) {
        double potency = PerkEffectService.medicalPotency(player, effectPerkId);
        if (flatHeal > 0f) {
            player.heal(flatHeal * (float) potency);
        }
        if (missingHealthPercent > 0) {
            float missing = player.getMaxHealth() - player.getHealth();
            if (missing > 0.01f) {
                player.heal((float) (missing * missingHealthPercent * potency));
            }
        }
        if (applyBuffs != null) {
            applyBuffs.run();
        }
        applyEfficientMedsBonus(player);
    }

    /** 仅百分比回血。 */
    public static void healMissingPercent(
            Player player, double missingPercent, String effectPerkId, boolean applyEfficientMeds) {
        applyAfterMedUse(player, 0f, missingPercent, effectPerkId, null);
    }

    /** Debuff 治愈类医疗品读条后的固定回血（6~20，材料越好越高）。 */
    public static float flatHealForDebuffItem(String itemPath) {
        return switch (itemPath) {
            case "bandage" -> 8f;
            case "hemostat" -> 10f;
            case "plaster_cast" -> 10f;
            case "antidotum_pills" -> 12f;
            case "vaccine" -> 14f;
            default -> 8f;
        };
    }

    /** 技能「专业医师」：额外恢复固定生命 + 已损失生命的 2%。 */
    public static void applyEfficientMedsBonus(Player player) {
        if (!PlayerFirearmsData.isUnlocked(player, "efficient_meds")) {
            return;
        }
        float missing = player.getMaxHealth() - player.getHealth();
        float bonus = 6f + missing * 0.02f;
        if (bonus > 0.01f) {
            player.heal(bonus);
        }
    }

    /**
     * 施加或强化 Buff。
     * <ul>
     *   <li>当前效果由本 mod 医疗品施加：等级 +1、持续时间叠加（连续用药才叠层）。</li>
     *   <li>来自药水/信标等外部来源：不叠层，取等级与剩余时间的较高者。</li>
     * </ul>
     */
    public static void applyOrUpgradeEffect(
            Player player, MobEffect effect, int durationTicks, int amplifierLevel) {
        MedicalEffectTracker.pruneStale(player);
        int newAmp = Math.max(0, amplifierLevel - 1);
        MobEffectInstance existing = player.getEffect(effect);

        if (existing == null) {
            player.addEffect(new MobEffectInstance(effect, durationTicks, newAmp, false, true, true));
            MedicalEffectTracker.markFromMedical(player, effect);
            return;
        }

        if (MedicalEffectTracker.isFromMedical(player, effect)) {
            int amp = Math.min(255, existing.getAmplifier() + 1);
            int dur = existing.getDuration() + durationTicks;
            player.addEffect(new MobEffectInstance(effect, dur, amp, false, true, true));
            MedicalEffectTracker.markFromMedical(player, effect);
            return;
        }

        int amp = Math.max(existing.getAmplifier(), newAmp);
        int dur = Math.max(existing.getDuration(), durationTicks);
        boolean medicalWins =
                amp > existing.getAmplifier()
                        || (amp == existing.getAmplifier() && dur > existing.getDuration());
        player.addEffect(new MobEffectInstance(effect, dur, amp, false, true, true));
        if (medicalWins) {
            MedicalEffectTracker.markFromMedical(player, effect);
        } else {
            MedicalEffectTracker.unmark(player, effect);
        }
    }

    /** 止血剂：暂停出血阶段恶化，并在一段时间内减缓恶化间隔。 */
    public static void applyHemostat(ServerPlayer player) {
        if (PlayerDebuffData.getStage(player, DebuffType.BLEED) <= 0) {
            player.displayClientMessage(
                    Component.translatable("gunsrpg.med.hemostat.no_bleed"), true);
            return;
        }
        int duration = hemostatDurationTicks(player);
        double intervalMult = hemostatIntervalMultiplier(player);
        PlayerDebuffData.setBleedProtection(player, duration, intervalMult);
        player.displayClientMessage(
                Component.translatable(
                        "gunsrpg.med.hemostat.applied",
                        duration / 20,
                        (int) ((intervalMult - 1.0) * 100)),
                true);
    }

    public static int hemostatDurationTicks(Player player) {
        double potency = PerkEffectService.medicalPotency(player, "hemostat_effect");
        int base = (int) (HEMOSTAT_BASE_SECONDS * 20 * potency);
        return base + medDelayBonusTicks(player, "bleeding_resistance");
    }

    /** 出血恶化间隔倍率（&gt;1 表示恶化更慢）。基础约 2×（等同原版 -50% 扩散速度）。 */
    public static double hemostatIntervalMultiplier(Player player) {
        double effectiveness = 0.5 * PerkEffectService.medicalPotency(player, "hemostat_effect");
        effectiveness = Math.max(0.25, Math.min(0.85, effectiveness));
        double medEffect = medEffectBonus(player, "bleeding_resistance");
        return (1.0 / effectiveness) * (1.0 + medEffect);
    }

    private static int medDelayBonusTicks(Player player, String resistancePrefix) {
        int tier = resistanceTier(player, resistancePrefix);
        if (tier <= 0) {
            return 0;
        }
        return switch (tier) {
            case 3 -> 60 * 20;
            case 2 -> 40 * 20;
            default -> 20 * 20;
        };
    }

    private static double medEffectBonus(Player player, String resistancePrefix) {
        int tier = resistanceTier(player, resistancePrefix);
        return switch (tier) {
            case 3 -> 0.30;
            case 2 -> 0.20;
            case 1 -> 0.10;
            default -> 0.0;
        };
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
}
