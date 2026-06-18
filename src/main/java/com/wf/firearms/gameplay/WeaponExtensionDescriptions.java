package com.wf.firearms.gameplay;

import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.ReloadStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** 枪械扩展天赋：面向玩家的动态数值说明（按枪身数据计算）。 */
public final class WeaponExtensionDescriptions {
    private static final double PUMP_IN_ACTION_RELOAD_MULT = 0.85;
    private static final double DEFT_RELOAD_MULT = 0.6;
    private static final double GEAR_GRINDER_RELOAD_MULT = 0.75;

    private static final String[] FIRE_INTERVAL_SUFFIXES = {
        "_adaptive_chambering",
        "_tough_spring",
        "_light_trigger",
        "_fast_hands",
        "_repeater"
    };

    private WeaponExtensionDescriptions() {}

    /** 缩短射击间隔类天赋：按枪身展示间隔（秒）与射速。 */
    public static List<String> fireIntervalLines(String skillId) {
        if ("mk14ebr_tough_spring".equals(skillId)) {
            return mk14ToughSpringLines();
        }
        String marker = fireIntervalSuffix(skillId);
        if (marker.isEmpty()) {
            return List.of();
        }
        String weaponKey = WeaponExtensionIds.weaponKeyFromSkillId(skillId, marker);
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        int base = spec.fireIntervalTicks();
        int after = intervalAfterTalent(base, skillId, marker);
        return intervalLines(base, after);
    }

    private static List<String> mk14ToughSpringLines() {
        FirearmSpec spec = FirearmRegistry.getOrDefault("mk14ebr");
        int base = spec.fireIntervalTicks();
        int semi = Math.max(1, base - 3);
        int auto = Math.max(1, base - 1);
        List<String> lines = new ArrayList<>();
        lines.add(
                String.format(
                        Locale.ROOT,
                        "半自动：射击间隔 %s → %s",
                        formatDuration(base),
                        formatDuration(semi)));
        lines.add(
                String.format(
                        Locale.ROOT,
                        "全自动：射击间隔 %s → %s",
                        formatDuration(base),
                        formatDuration(auto)));
        return lines;
    }

    /** 「机关润滑 / 精密零件」等：实际为换弹加速，非射速。 */
    public static List<String> gearGrinderLines(String skillId) {
        if (skillId == null || !skillId.endsWith("_gear_grinder")) {
            return List.of();
        }
        String weaponKey = WeaponExtensionIds.weaponKeyFromSkillId(skillId, "_gear_grinder");
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        List<String> lines = new ArrayList<>();
        lines.add(String.format(Locale.ROOT, "换弹时间减少 %d%%", reloadReductionPercent(GEAR_GRINDER_RELOAD_MULT)));
        if (spec.reloadStyle() == ReloadStyle.SHELL_BY_SHELL) {
            int baseShell = shellBaseTicks(spec);
            lines.add(reloadDeltaLine("每发装填", baseShell, scaledTicks(baseShell, GEAR_GRINDER_RELOAD_MULT)));
        } else {
            int base = spec.reloadTicks();
            lines.add(reloadDeltaLine("整匣换弹", base, scaledTicks(base, GEAR_GRINDER_RELOAD_MULT)));
        }
        return lines;
    }

    public static List<String> pumpInActionLines(String skillId) {
        if (skillId == null || !skillId.endsWith("_pump_in_action")) {
            return List.of();
        }
        String weaponKey = WeaponExtensionIds.weaponKeyFromSkillId(skillId, "_pump_in_action");
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        List<String> lines = new ArrayList<>();
        lines.add(String.format(Locale.ROOT, "装填时间减少 %d%%", reloadReductionPercent(PUMP_IN_ACTION_RELOAD_MULT)));

        if (spec.reloadStyle() == ReloadStyle.SHELL_BY_SHELL) {
            int baseShell = shellBaseTicks(spec);
            int afterShell = scaledTicks(baseShell, PUMP_IN_ACTION_RELOAD_MULT);
            lines.add(reloadDeltaLine("每发装填", baseShell, afterShell));

            int basePrep = spec.reloadPrepTicks();
            if (basePrep > 0) {
                int afterPrep = scaledTicks(basePrep, PUMP_IN_ACTION_RELOAD_MULT);
                lines.add(reloadDeltaLine("空仓首发额外上膛", basePrep, afterPrep));
                lines.add(
                        String.format(
                                Locale.ROOT,
                                "空仓开始装填首发的合计：%s → %s",
                                formatDuration(baseShell + basePrep),
                                formatDuration(afterShell + afterPrep)));
            }
        } else {
            int base = spec.reloadTicks();
            lines.add(reloadDeltaLine("整匣装填", base, scaledTicks(base, PUMP_IN_ACTION_RELOAD_MULT)));
        }
        return lines;
    }

    public static List<String> deftReloadLines(String skillId) {
        if (skillId == null || !skillId.endsWith("_deft_reload")) {
            return List.of();
        }
        String weaponKey = WeaponExtensionIds.weaponKeyFromSkillId(skillId, "_deft_reload");
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        List<String> lines = new ArrayList<>();
        lines.add(String.format(Locale.ROOT, "换弹时间减少 %d%%", reloadReductionPercent(DEFT_RELOAD_MULT)));

        if (spec.reloadStyle() == ReloadStyle.SHELL_BY_SHELL) {
            int baseShell = shellBaseTicks(spec);
            lines.add(reloadDeltaLine("每发装填", baseShell, scaledTicks(baseShell, DEFT_RELOAD_MULT)));
        } else {
            int base = spec.reloadTicks();
            lines.add(reloadDeltaLine("整匣装填", base, scaledTicks(base, DEFT_RELOAD_MULT)));
        }
        return lines;
    }

    /** @deprecated 使用 {@link #fireIntervalLines} */
    public static List<String> toughSpringLines(String skillId) {
        return fireIntervalLines(skillId);
    }

    public static boolean usesDynamicFireIntervalDescription(String skillId) {
        return !fireIntervalSuffix(skillId).isEmpty();
    }

    private static String fireIntervalSuffix(String skillId) {
        if (skillId == null) {
            return "";
        }
        for (String marker : FIRE_INTERVAL_SUFFIXES) {
            if (skillId.endsWith(marker)) {
                return marker;
            }
        }
        return "";
    }

    /** 与 {@link WeaponExtensionService#apply} 中单项天赋一致。 */
    private static int intervalAfterTalent(int base, String skillId, String marker) {
        return switch (marker) {
            case "_tough_spring" ->
                    Math.max(1, base + ("deagle_tough_spring".equals(skillId) ? -5 : -1));
            case "_light_trigger" -> Math.max(1, (int) Math.round(base * 0.9));
            case "_adaptive_chambering" -> Math.max(1, base - 3);
            case "_repeater" -> Math.max(1, (int) Math.round(base * 0.8));
            case "_fast_hands" -> Math.max(1, (int) Math.round(base * 0.7));
            default -> base;
        };
    }

    private static List<String> intervalLines(int base, int after) {
        List<String> lines = new ArrayList<>();
        lines.add(
                String.format(
                        Locale.ROOT,
                        "射击间隔：%s → %s（缩短 %s）",
                        formatDuration(base),
                        formatDuration(after),
                        formatDuration(Math.max(0, base - after))));
        if (base > 0 && after > 0) {
            lines.add(String.format(Locale.ROOT, "约 %.1f 发/秒 → %.1f 发/秒", 20.0 / base, 20.0 / after));
        }
        return lines;
    }

    private static int shellBaseTicks(FirearmSpec spec) {
        return spec.shellReloadTicks() > 0 ? spec.shellReloadTicks() : spec.reloadTicks();
    }

    private static int scaledTicks(int base, double extMult) {
        return Math.max(5, (int) Math.round(base * extMult));
    }

    private static int reloadReductionPercent(double extMult) {
        return (int) Math.round((1.0 - extMult) * 100.0);
    }

    private static String reloadDeltaLine(String label, int baseTicks, int afterTicks) {
        return String.format(
                Locale.ROOT,
                "%s：%s → %s（减少 %s）",
                label,
                formatDuration(baseTicks),
                formatDuration(afterTicks),
                formatDuration(Math.max(0, baseTicks - afterTicks)));
    }

    private static String formatDuration(int ticks) {
        double seconds = ticks / 20.0;
        if (Math.abs(seconds - Math.rint(seconds)) < 0.001) {
            return String.format(Locale.ROOT, "%.0f 秒", seconds);
        }
        return String.format(Locale.ROOT, "%.2f 秒", seconds);
    }
}
