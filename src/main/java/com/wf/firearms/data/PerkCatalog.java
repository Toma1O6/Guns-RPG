package com.wf.firearms.data;

import java.util.Set;

/** 本整合包启用的属性天赋；未列出且不在排除表中的 perk 仍从配置加载。 */
public final class PerkCatalog {
    /** 原版 Guns RPG 有定义但本包未实装 —— 不加载、不显示。 */
    private static final Set<String> EXCLUDED =
            Set.of(
                    "iron_buddy_cooldown",
                    "second_chance_cooldown",
                    "quest_ammo",
                    "perk_book_chance",
                    "skill_book_chance");

    /** 减少类天赋正投资上限（50%）。 */
    public static final double REDUCTION_BUFF_CAP = 0.5;

    /** 正投资降低负面效果；UI 显示「−X%」而非「+X%」。 */
    private static final Set<String> REDUCTION_DISPLAY =
            Set.of(
                    "damage_taken",
                    "fall_damage",
                    "reload_speed",
                    "recoil_control",
                    "weapon_jamming",
                    "weapon_durability",
                    "weapon_noise",
                    "unjamming_speed",
                    "melee_cooldown",
                    "airdrop_call_cooldown");

    private PerkCatalog() {}

    public static boolean isEnabled(String perkId) {
        return perkId != null && !EXCLUDED.contains(perkId);
    }

    public static boolean isReductionDisplay(String perkId) {
        return perkId != null && REDUCTION_DISPLAY.contains(perkId);
    }
}
