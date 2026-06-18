package com.wf.firearms.client.gui;

import com.wf.firearms.data.SkillDisplayNames;
import net.minecraft.network.chat.Component;

import java.util.Locale;
import java.util.Map;

/** 技能树 UI 文案：语言包缺失时的中文兜底（整合包默认中文界面）。 */
public final class SkillUiText {
    private static final Map<String, String> CATEGORY_ZH = Map.of(
            "gun", "枪械",
            "survival", "生存",
            "mining", "采矿",
            "resistance", "抗性");

    private static final Map<String, String> DESC_ZH = Map.ofEntries(
            Map.entry("bone_grinder_i", "在枪械台用骨头制作骨粉，单次产出 3 个"),
            Map.entry("bone_grinder_ii", "在枪械台用骨头制作骨粉，单次产出 5 个"),
            Map.entry("bone_grinder_iii", "在枪械台用骨头制作骨粉，单次产出 8 个"),
            Map.entry("m1911_assembly", "解锁 M1911 手枪装配与枪械台配方（入门手枪）"),
            Map.entry("glock_assembly", "解锁格洛克手枪装配与枪械台配方"),
            Map.entry("wooden_ammo_smith", "允许在枪械台制作弹壳及木制子弹"),
            Map.entry("gunpowder_novice", "允许制作火药，单次产出 2 个"),
            Map.entry("gunpowder_expert", "允许制作火药，单次产出 4 个"),
            Map.entry("gunpowder_master", "允许制作火药，单次产出 6 个"),
            Map.entry("gun_parts_smith", "允许制作枪械零件"));

    private SkillUiText() {}

    public static Component titleComponent(String id) {
        return Component.literal(SkillDisplayNames.resolveTitle(id));
    }

    public static String title(String id) {
        return SkillDisplayNames.resolveTitle(id);
    }

    public static String categoryLabel(String categoryId) {
        Component c = Component.translatable("gunsrpg.category." + categoryId);
        String s = c.getString();
        if (!s.startsWith("gunsrpg.category.")) {
            return s;
        }
        return CATEGORY_ZH.getOrDefault(categoryId, categoryId);
    }

    public static String descriptionFallback(String skillId) {
        return DESC_ZH.getOrDefault(skillId, "");
    }
}
