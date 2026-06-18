package com.wf.firearms.client.gui;

import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.SkillDisplayNames;
import com.wf.firearms.data.SkillNode;
import com.wf.firearms.gameplay.SkillUnlockService;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/** 技能树右侧详情：面向玩家的中文说明。 */
public final class SkillDetailFormatter {
    private SkillDetailFormatter() {}

    public static String title(SkillNode node) {
        String id = node.getId();
        try {
            return SkillDisplayNames.resolveTitle(id);
        } catch (LinkageError | Exception ex) {
            return id == null || id.isEmpty() ? "?" : id.replace('_', ' ');
        }
    }

    public static String description(SkillNode node) {
        List<String> lines = SkillDisplayNames.resolveDescriptionLines(node.getId());
        if (!lines.isEmpty()) {
            return String.join("\n", lines);
        }
        return SkillUiText.descriptionFallback(node.getId());
    }

    public static List<String> bodyLines(SkillNode node, Player player) {
        List<String> lines = new ArrayList<>();

        List<String> descLines = SkillDisplayNames.resolveDescriptionLines(node.getId());
        if (descLines.isEmpty()) {
            String fb = SkillUiText.descriptionFallback(node.getId());
            if (!fb.isEmpty()) {
                descLines = List.of(fb);
            } else if (node.getId().endsWith("_assembly")) {
                descLines = List.of("解锁后可制作并使用该枪械");
            }
        }
        for (String line : descLines) {
            lines.add("§7" + line);
        }

        if (SkillUnlockService.usesExtensionPoints(node)) {
            lines.add(String.format(Locale.ROOT, "消耗 %d 扩展点", node.getPrice()));
        } else if (node.getPrice() > 0) {
            lines.add(String.format(Locale.ROOT, "消耗 %d 技能点", node.getPrice()));
        }

        appendLevelRequirement(lines, node);
        appendParentRequirement(lines, node, player);

        return lines;
    }

    public static String statusLine(SkillNode node, Player player) {
        if (PlayerFirearmsData.isUnlocked(player, node.getId())) {
            return "§a§l已解锁";
        }
        Optional<String> preview = SkillUnlockService.tryUnlockPreview(player, node.getId());
        if (preview.isPresent()) {
            return "§c" + preview.get();
        }
        return "§e满足条件，可以解锁";
    }

    /** 始终显示解锁等级要求，避免仅因前置未解锁时看不到等级门槛。 */
    private static void appendLevelRequirement(List<String> lines, SkillNode node) {
        int req = node.getLevel();
        if (req <= 0) {
            return;
        }
        String type = node.getValidatorType();
        if ("gunsrpg:weapon".equals(type)) {
            String weaponKey = node.getValidatorData();
            String name = weaponKey.isEmpty()
                    ? "对应枪械"
                    : SkillDisplayNames.resolveTitle(weaponKey + "_assembly");
            lines.add(String.format(Locale.ROOT, "解锁等级：%s 熟练度 ≥ %d", name, req));
        } else if ("gunsrpg:level".equals(type) || type.isEmpty()) {
            lines.add(String.format(Locale.ROOT, "解锁等级：火器等级 ≥ %d", req));
        }
    }

    private static void appendParentRequirement(List<String> lines, SkillNode node, Player player) {
        String parent = node.getParent();
        if (parent.isEmpty() || PlayerFirearmsData.isUnlocked(player, parent)) {
            return;
        }
        lines.add("前置：" + SkillDisplayNames.resolveTitle(parent));
    }
}
