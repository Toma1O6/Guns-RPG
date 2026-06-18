package com.wf.firearms.client.gui;

import com.wf.firearms.data.PerkCatalog;
import com.wf.firearms.data.PerkDef;
import com.wf.firearms.data.SkillDatabase;
import net.minecraft.network.chat.Component;

import java.util.Locale;
import java.util.Map;

/** 天赋说明与实现状态。 */
public final class PerkUiText {
    private static final Map<String, String> DESCRIPTIONS = Map.ofEntries(
            Map.entry("pistol_damage", "手枪类武器伤害"),
            Map.entry("smg_damage", "冲锋枪类伤害"),
            Map.entry("ar_damage", "突击步枪类伤害"),
            Map.entry("dmr_damage", "精确射手步枪类伤害"),
            Map.entry("sr_damage", "狙击类伤害"),
            Map.entry("shotgun_damage", "霰弹枪类伤害"),
            Map.entry("bow_damage", "弓弩类伤害"),
            Map.entry("silent_weapon_damage", "消音武器伤害（半效）"),
            Map.entry("loud_weapon_damage", "高噪武器伤害（半效）"),
            Map.entry("mining_speed", "采矿速度"),
            Map.entry("digging_speed", "挖掘速度"),
            Map.entry("woodcutting_speed", "伐木速度"),
            Map.entry("movement_speed", "移动速度"),
            Map.entry("damage_taken", "所受伤害"),
            Map.entry("fall_damage", "摔落伤害"),
            Map.entry("bleed_resistance", "出血触发抗性"),
            Map.entry("bleed_delay", "出血恶化延迟"),
            Map.entry("fracture_resistance", "骨折触发抗性"),
            Map.entry("fracture_delay", "骨折恶化延迟"),
            Map.entry("poison_resistance", "中毒触发抗性"),
            Map.entry("poison_delay", "中毒恶化延迟"),
            Map.entry("infection_resistance", "感染触发抗性"),
            Map.entry("infection_delay", "感染恶化延迟"),
            Map.entry("bandage_effect", "绷带多减出血层数"),
            Map.entry("hemostat_effect", "止血剂额外层数"),
            Map.entry("morphine_effect", "吗啡疗效"),
            Map.entry("adrenaline_effect", "肾上腺素疗效"),
            Map.entry("calcium_shot_effect", "抗毒剂疗效"),
            Map.entry("propital_effect", "抑制剂疗效"),
            Map.entry("splint_effect", "夹板疗效"),
            Map.entry("steroids_effect", "类固醇疗效"),
            Map.entry("vitamins_effect", "维生素疗效"),
            Map.entry("vaccine_effect", "疫苗疗效"),
            Map.entry("like_a_cat_effect", "猫步（坠落）"),
            Map.entry("reload_speed", "装弹耗时"),
            Map.entry("recoil_control", "后坐力"),
            Map.entry("unjamming_speed", "排障耗时"),
            Map.entry("weapon_jamming", "卡弹几率"),
            Map.entry("weapon_durability", "武器射击损耗"),
            Map.entry("weapon_noise", "枪声大小"),
            Map.entry("headshot_damage", "高位瞄准额外伤害"),
            Map.entry("melee_damage", "近战伤害"),
            Map.entry("melee_cooldown", "近战冷却"),
            Map.entry("motherlode_bonus", "采矿额外经验"),
            Map.entry("well_fed_duration", "食物效果时长"),
            Map.entry("airdrop_call_cooldown", "紧急空投技能冷却（最多 −80%）"));

    private PerkUiText() {}

    public static Component perkTitleComponent(String perkId) {
        return Component.translatable("perk.gunsrpg." + perkId);
    }

    /** 已翻译的显示名；无语言条目时用 DESCRIPTIONS 或 id。 */
    public static String perkTitlePlain(String perkId) {
        Component c = perkTitleComponent(perkId);
        String s = c.getString();
        if (!s.startsWith("perk.gunsrpg.")) {
            return s;
        }
        return DESCRIPTIONS.getOrDefault(perkId, perkId.replace('_', ' '));
    }

    public static String statusTag(String perkId) {
        return "§a[已实现]";
    }

    public static String describe(String perkId) {
        String base = DESCRIPTIONS.getOrDefault(perkId, "全局被动天赋");
        return SkillDatabase.getPerk(perkId)
                .map(p -> formatWithScaling(base, p))
                .orElse(base);
    }

    /** 列表行百分比：减少类显示 −X%，增益类显示 +X%。 */
    public static String formatPercentDisplay(String perkId, double currentPercent) {
        if (PerkCatalog.isReductionDisplay(perkId)) {
            return String.format(Locale.ROOT, "−%.1f%%", Math.abs(currentPercent));
        }
        return String.format(Locale.ROOT, "+%.1f%%", currentPercent);
    }

    private static String formatWithScaling(String base, PerkDef p) {
        double stepPct = p.getScaling() * 100;
        double maxBuff = p.getBuffBound() * 100;
        if (PerkCatalog.isReductionDisplay(p.getId())) {
            return String.format(Locale.ROOT, "降低%s。每步 −%.2f%%，最多约 −%.0f%%", base, stepPct, maxBuff);
        }
        return String.format(Locale.ROOT, "提升%s。每步 +%.2f%%，上限约 +%.0f%%", base, stepPct, maxBuff);
    }

    public static String detailBlock(String perkId, int invest) {
        StringBuilder sb = new StringBuilder();
        sb.append(statusTag(perkId)).append(' ').append(describe(perkId));
        if (invest > 0) {
            sb.append(String.format(Locale.ROOT, " §e当前 %d 步。", invest));
        }
        return sb.toString();
    }
}
