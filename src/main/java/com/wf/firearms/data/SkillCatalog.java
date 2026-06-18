package com.wf.firearms.data;



import java.util.Set;



/** 本整合包禁用或未实装的技能节点。 */

public final class SkillCatalog {

    private static final Set<String> EXCLUDED =

            Set.of(

                    // 原版任务/探索 —— 由 FTB 任务替代

                    "treasure_hunter_i",

                    "treasure_hunter_ii",

                    "treasure_hunter_iii",

                    "traps_i",

                    "traps_ii",

                    "traps_iii",

                    "bartender_i",

                    "bartender_ii",

                    "bartender_iii",

                    "bartender_iv",

                    "bartender_v",

                    // 主动/召唤类（仅保留 god_help_us）

                    "iron_buddy_i",

                    "iron_buddy_ii",

                    "iron_buddy_iii",

                    "avenge_me_friends",

                    // 暂不需要的 mining 链

                    "crystal_station",

                    "crystal_forge",

                    "crystal_purification_station",

                    "crystalized",

                    "blacksmith",

                    "blaze_powder_i",

                    "blaze_powder_ii",

                    "blaze_powder_iii",

                    "hammer_i",

                    "hammer_ii",

                    "hammer_iii",

                    "mineralogist",

                    // 整合包禁用

                    "like_a_cat_i",

                    "like_a_cat_ii",

                    "like_a_cat_iii",

                    "like_a_cat_iv",

                    "like_a_cat_v",

                    // 弩/连弩 —— 暂缓

                    "crossbow_assembly",

                    "chukonu_assembly",

                    "demolisher",

                    "ammo_smithing_mastery",

                    // 已由轻机枪线 pkm → m249 → gatling 取代

                    "minigun_assembly",

                    // 冲锋枪线改为 ump45 → vector → uzi → p90

                    "thompson_assembly",

                    "vss_assembly");

                    // 突击步枪：akm → hk416 → type_81 → aug（见 extra_skills / port akm 子节点）
                    // 射手步枪：fn_fal → sks → spr15 → mk14（gun_parts_smith 并列根）



    private SkillCatalog() {}



    public static boolean isEnabled(String skillId) {

        if (skillId == null || EXCLUDED.contains(skillId)) {

            return false;

        }

        if (skillId.startsWith("crossbow_") || skillId.startsWith("chukonu_") || skillId.startsWith("vss_")) {

            return false;

        }

        return true;

    }



    /** 是否可在技能树中主动使用的技能（目前仅紧急空投）。 */

    public static boolean isActiveSkill(String skillId) {

        return "god_help_us".equals(skillId);

    }

}

