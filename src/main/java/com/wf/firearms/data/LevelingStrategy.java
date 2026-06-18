package com.wf.firearms.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/** 解析 {@code leveling_strategy.json}（Guns RPG 玩家/武器等级曲线）。 */
public final class LevelingStrategy {
    private final LevelCurve playerLevel;
    private final LevelCurve weaponLevel;

    private LevelingStrategy(LevelCurve playerLevel, LevelCurve weaponLevel) {
        this.playerLevel = playerLevel;
        this.weaponLevel = weaponLevel;
    }

    public static LevelingStrategy load() {
        if (!Files.isRegularFile(PortPaths.levelingStrategyFile())) {
            return defaults();
        }
        try (Reader reader = Files.newBufferedReader(PortPaths.levelingStrategyFile(), StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject defs = root.getAsJsonObject("definitions");
            return new LevelingStrategy(
                    LevelCurve.parse(defs.getAsJsonObject("playerLevel")),
                    LevelCurve.parse(defs.getAsJsonObject("weaponLevel")));
        } catch (IOException e) {
            return defaults();
        }
    }

    private static LevelingStrategy defaults() {
        return new LevelingStrategy(LevelCurve.defaultPlayer(), LevelCurve.defaultWeapon());
    }

    public LevelCurve playerLevel() {
        return playerLevel;
    }

    public LevelCurve weaponLevel() {
        return weaponLevel;
    }

    public static final class LevelCurve {
        private final int maxLevel;
        private final List<KillRequirement> killRequirements;
        private final List<LevelReward> rewards;
        /** 逐级击杀：升到 L 级本段需 {@code tieredLinearBase * L} 杀（0→1 为 base×1）。 */
        private final boolean tieredLinear;
        private final int tieredLinearBase;

        private LevelCurve(
                int maxLevel,
                List<KillRequirement> killRequirements,
                List<LevelReward> rewards,
                boolean tieredLinear,
                int tieredLinearBase) {
            this.maxLevel = maxLevel;
            this.killRequirements = killRequirements;
            this.rewards = rewards;
            this.tieredLinear = tieredLinear;
            this.tieredLinearBase = Math.max(1, tieredLinearBase);
        }

        static LevelCurve parse(JsonObject obj) {
            int max = obj.has("maxLevel") ? obj.get("maxLevel").getAsInt() : 100;
            boolean tiered = false;
            int tierBase = 5;
            if (obj.has("progression") && obj.get("progression").isJsonObject()) {
                JsonObject prog = obj.getAsJsonObject("progression");
                if ("tieredLinear".equals(prog.has("type") ? prog.get("type").getAsString() : "")) {
                    tiered = true;
                    if (prog.has("killsForFirstLevel")) {
                        tierBase = prog.get("killsForFirstLevel").getAsInt();
                    } else if (prog.has("perLevelBase")) {
                        tierBase = prog.get("perLevelBase").getAsInt();
                    }
                }
            }
            List<KillRequirement> reqs = new ArrayList<>();
            if (!tiered && obj.has("killRequirements")) {
                for (JsonElement el : obj.getAsJsonArray("killRequirements")) {
                    reqs.add(KillRequirement.parse(el.getAsJsonObject()));
                }
            }
            List<LevelReward> rewards = new ArrayList<>();
            if (obj.has("rewards")) {
                for (JsonElement el : obj.getAsJsonArray("rewards")) {
                    rewards.add(LevelReward.parse(el.getAsJsonObject()));
                }
            }
            return new LevelCurve(max, reqs, rewards, tiered, tierBase);
        }

        static LevelCurve defaultPlayer() {
            return new LevelCurve(100, List.of(), List.of(new LevelReward("true", 0, 2, List.of())), true, 5);
        }

        /** 累计击杀达到该等级所需总数（0 级为 0）。 */
        public static int cumulativeKillsForLevel(int level, int baseKills) {
            if (level <= 0) {
                return 0;
            }
            return baseKills * level * (level + 1) / 2;
        }

        /** 从 {@code fromLevel} 升到 {@code fromLevel+1} 本段所需击杀。 */
        public static int killsForStep(int fromLevel, int baseKills) {
            return baseKills * (fromLevel + 1);
        }

        static LevelCurve defaultWeapon() {
            List<KillRequirement> reqs = new ArrayList<>();
            reqs.add(new KillRequirement("equal", 1, 5));
            reqs.add(new KillRequirement("equal", 2, 20));
            reqs.add(new KillRequirement("equal", 3, 40));
            reqs.add(new KillRequirement("equal", 4, 70));
            reqs.add(new KillRequirement("equal", 5, 110));
            reqs.add(new KillRequirement("true", 0, Integer.MAX_VALUE / 4));
            return new LevelCurve(9, reqs, List.of(new LevelReward("true", 0, 1, List.of())), false, 5);
        }

        public int maxLevel() {
            return maxLevel;
        }

        public boolean usesTieredLinearProgression() {
            return tieredLinear;
        }

        public int tieredLinearBase() {
            return tieredLinearBase;
        }

        /** 根据击杀数计算等级。 */
        public int levelForKills(int kills) {
            if (tieredLinear) {
                int level = 0;
                while (level < maxLevel
                        && cumulativeKillsForLevel(level + 1, tieredLinearBase) <= kills) {
                    level++;
                }
                return level;
            }
            int level = 0;
            for (KillRequirement req : killRequirements) {
                if (req.matches(kills)) {
                    level = Math.max(level, req.levelHint());
                }
            }
            return Math.min(level, maxLevel);
        }

        /** 升到 {@code level} 时发放的技能/扩展点（仅该等级一档）。 */
        public int pointsGrantedAtLevel(int level) {
            int total = 0;
            for (LevelReward reward : rewards) {
                if (reward.appliesToLevel(level)) {
                    total += reward.pointAmount();
                }
            }
            return total;
        }

        /** 达到 {@code targetLevel} 所需的累计击杀（展示用）。 */
        public int killsRequiredForLevel(int targetLevel) {
            if (tieredLinear) {
                return cumulativeKillsForLevel(targetLevel, tieredLinearBase);
            }
            int best = Integer.MAX_VALUE;
            for (KillRequirement req : killRequirements) {
                if (req.function.equals("equal") && req.levelHint() == targetLevel) {
                    best = Math.min(best, req.requiredKills());
                }
                if (req.function.equals("biggerOrEqual") && req.levelHint() == targetLevel) {
                    best = Math.min(best, req.requiredKills());
                }
            }
            return best == Integer.MAX_VALUE ? 0 : best;
        }
    }

    public record KillRequirement(String function, int levelValue, int requiredKills) {
        static KillRequirement parse(JsonObject o) {
            String fn = o.has("function") ? o.get("function").getAsString() : "true";
            int value = o.has("value") ? o.get("value").getAsInt() : 0;
            int require = o.has("require") ? o.get("require").getAsInt() : 0;
            return new KillRequirement(fn, value, require);
        }

        boolean matches(int kills) {
            return kills >= requiredKills;
        }

        int levelHint() {
            if ("true".equals(function)) {
                return requiredKills <= 5 ? 1 : 0;
            }
            return levelValue;
        }
    }

    public record LevelReward(String function, int value, int pointAmount, java.util.List<Integer> oneOfList) {
        static LevelReward parse(JsonObject o) {
            String fn = o.has("function") ? o.get("function").getAsString() : "true";
            int value = o.has("value") ? o.get("value").getAsInt() : 0;
            int amount = 0;
            java.util.List<Integer> oneOf = java.util.List.of();
            if ("oneOf".equals(fn) && o.has("list")) {
                oneOf = new java.util.ArrayList<>();
                for (JsonElement el : o.getAsJsonArray("list")) {
                    oneOf.add(el.getAsInt());
                }
            }
            if (o.has("rewards")) {
                for (JsonElement el : o.getAsJsonArray("rewards")) {
                    JsonObject r = el.getAsJsonObject();
                    if ("gunsrpg:point".equals(r.get("type").getAsString()) && r.has("amount")) {
                        amount += r.get("amount").getAsInt();
                    }
                }
            }
            return new LevelReward(fn, value, amount, oneOf);
        }

        boolean appliesToLevel(int level) {
            return switch (function) {
                case "equal" -> level == value;
                case "each" -> value > 0 && level % value == 0;
                case "oneOf" -> oneOfList.contains(level);
                case "true" -> true;
                default -> level > 0;
            };
        }
    }
}
