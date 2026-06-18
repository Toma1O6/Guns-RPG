package com.wf.firearms.debuff;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PortPaths;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class DebuffConfig {
    private static DebuffRules bleed = DebuffRules.defaultsBleed();
    private static DebuffRules fracture = DebuffRules.defaultsFracture();
    private static DebuffRules poison = DebuffRules.defaultsPoison();
    private static DebuffRules infection = DebuffRules.defaultsInfection();

    private DebuffConfig() {}

    public static void reload() {
        var file = PortPaths.configRoot().resolve("debuff_config.json");
        if (!Files.isRegularFile(file)) {
            GunsRpg.LOGGER.warn("[gunsrpg] 使用内置 debuff 默认配置");
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            bleed = DebuffRules.parse(root.getAsJsonObject("bleed"), bleed);
            fracture = DebuffRules.parse(root.getAsJsonObject("fracture"), fracture);
            poison = DebuffRules.parse(root.getAsJsonObject("poison"), poison);
            infection = DebuffRules.parse(root.getAsJsonObject("infection"), infection);
            GunsRpg.LOGGER.info("[gunsrpg] 已加载 debuff 配置");
        } catch (IOException e) {
            GunsRpg.LOGGER.error("[gunsrpg] debuff_config.json 读取失败", e);
        }
    }

    public static DebuffRules bleed() {
        return bleed;
    }

    public static DebuffRules fracture() {
        return fracture;
    }

    public static DebuffRules poison() {
        return poison;
    }

    public static DebuffRules infection() {
        return infection;
    }

    public record DebuffRules(
            double hurtProcChance,
            double fallMinDamage,
            double fallProcChance,
            int maxStage,
            int stageIntervalTicks,
            int dotIntervalTicks,
            double dotPerStage,
            double dotFlatPerStage,
            double dotMaxHealthPercentPerStage,
            int durationTicks,
            int slownessAmplifier,
            int miningFatigueAmplifier,
            double movementSpeedPenalty,
            double attackSpeedPenalty,
            float sprintDamage,
            float jumpDamage,
            int bleedStageRequire,
            double spreadChance,
            double[] skillResist,
            int[] skillDelayTicks,
            int progressFillTicks,
            int[] dotIntervalByStage) {

        /** 按阶段取 DoT 间隔；未配置数组时退回统一 {@link #dotIntervalTicks}。 */
        public int dotIntervalForStage(int stage) {
            if (dotIntervalByStage != null && dotIntervalByStage.length > 0 && stage > 0) {
                return dotIntervalByStage[Math.min(stage - 1, dotIntervalByStage.length - 1)];
            }
            return dotIntervalTicks;
        }

        /** 骨折 HUD 进度条填满所需 tick；0 表示总时长的一半。 */
        public int progressFillFor(int fractureDurationTicks) {
            if (progressFillTicks > 0) {
                return progressFillTicks;
            }
            return Math.max(1, fractureDurationTicks / 2);
        }

        static DebuffRules defaultsBleed() {
            return new DebuffRules(
                    0.14, 0, 0, 4, 1200, 40, 0, 1.0, 0.01, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    new double[] {0.12, 0.24, 0.36},
                    new int[] {100, 220, 360},
                    0,
                    new int[] {120, 80, 60, 30});
        }

        static DebuffRules defaultsFracture() {
            return new DebuffRules(
                    0.06, 3.5, 0.22, 1, 0, 0, 0, 0, 0, 7200, 1, 1, 0.35, 0.30, 0.8f, 0.5f, 0, 0,
                    new double[] {0.12, 0.24, 0.36},
                    new int[] {100, 220, 360},
                    0,
                    null);
        }

        static DebuffRules defaultsPoison() {
            return new DebuffRules(
                    0.08, 0, 0, 3, 1600, 50, 0.25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    new double[] {0.12, 0.24, 0.36},
                    new int[] {100, 220, 360},
                    0,
                    null);
        }

        static DebuffRules defaultsInfection() {
            return new DebuffRules(
                    0, 0, 0, 3, 1800, 60, 0.3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0.18,
                    new double[] {0.12, 0.24, 0.36},
                    new int[] {100, 220, 360},
                    0,
                    null);
        }

        static DebuffRules parse(JsonObject o, DebuffRules fallback) {
            if (o == null) {
                return fallback;
            }
            double dotFlat = fallback.dotFlatPerStage();
            double dotPct = fallback.dotMaxHealthPercentPerStage();
            if (o.has("dot_damage")) {
                JsonObject dot = o.getAsJsonObject("dot_damage");
                dotFlat = d(dot, "flat_per_stage", dotFlat);
                dotPct = d(dot, "max_health_percent_per_stage", dotPct);
            } else {
                dotFlat = d(o, "dot_flat_per_stage", dotFlat);
                dotPct = d(o, "dot_max_health_percent_per_stage", dotPct);
            }
            return new DebuffRules(
                    d(o, "hurt_proc_chance", fallback.hurtProcChance),
                    d(o, "fall_min_damage", fallback.fallMinDamage),
                    d(o, "fall_proc_chance", fallback.fallProcChance),
                    i(o, "max_stage", fallback.maxStage),
                    i(o, "stage_interval_ticks", fallback.stageIntervalTicks),
                    i(o, "dot_interval_ticks", fallback.dotIntervalTicks),
                    d(o, "dot_per_stage", fallback.dotPerStage),
                    dotFlat,
                    dotPct,
                    i(o, "duration_ticks", fallback.durationTicks),
                    i(o, "slowness_amplifier", fallback.slownessAmplifier),
                    i(o, "mining_fatigue_amplifier", fallback.miningFatigueAmplifier),
                    d(o, "movement_speed_penalty", fallback.movementSpeedPenalty()),
                    d(o, "attack_speed_penalty", fallback.attackSpeedPenalty()),
                    f(o, "sprint_damage", fallback.sprintDamage),
                    f(o, "jump_damage", fallback.jumpDamage),
                    i(o, "bleed_stage_require", fallback.bleedStageRequire),
                    d(o, "spread_chance", fallback.spreadChance),
                    arrD(o, "skill_resist", fallback.skillResist),
                    arrI(o, "skill_delay_ticks", fallback.skillDelayTicks),
                    i(o, "progress_fill_ticks", fallback.progressFillTicks),
                    arrI(o, "dot_interval_by_stage", fallback.dotIntervalByStage));
        }

        private static double d(JsonObject o, String k, double def) {
            return o.has(k) ? o.get(k).getAsDouble() : def;
        }

        private static int i(JsonObject o, String k, int def) {
            return o.has(k) ? o.get(k).getAsInt() : def;
        }

        private static float f(JsonObject o, String k, float def) {
            return o.has(k) ? o.get(k).getAsFloat() : def;
        }

        private static double[] arrD(JsonObject o, String k, double[] def) {
            if (!o.has(k)) {
                return def;
            }
            JsonArray a = o.getAsJsonArray(k);
            double[] out = new double[a.size()];
            for (int i = 0; i < a.size(); i++) {
                out[i] = a.get(i).getAsDouble();
            }
            return out;
        }

        private static int[] arrI(JsonObject o, String k, int[] def) {
            if (!o.has(k)) {
                return def;
            }
            JsonArray a = o.getAsJsonArray(k);
            int[] out = new int[a.size()];
            for (int i = 0; i < a.size(); i++) {
                out[i] = a.get(i).getAsInt();
            }
            return out;
        }
    }
}
