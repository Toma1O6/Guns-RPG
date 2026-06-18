package com.wf.firearms.data;

import com.wf.firearms.gameplay.MiningSkillService;
import com.wf.firearms.gameplay.WeaponExtensionService;

import java.util.Locale;

/** 技能说明占位符数值（对齐原版 Guns RPG Modifiers / MotherlodeSkill）。 */
public final class SkillDescriptionValues {
    private SkillDescriptionValues() {}

    public static int tier(String skillId) {
        if (skillId.endsWith("_iii")) {
            return 3;
        }
        if (skillId.endsWith("_ii")) {
            return 2;
        }
        if (skillId.endsWith("_iv")) {
            return 4;
        }
        if (skillId.endsWith("_v")) {
            return 5;
        }
        if (skillId.endsWith("_i")) {
            return 1;
        }
        return 1;
    }

    public static Object[] argsFor(String skillId, String suffix) {
        int t = tier(skillId);
        return switch (suffix) {
            case "damage" -> new Object[] {strongMusclesDisplay(t)};
            case "chance" -> chanceArgs(skillId, t);
            case "predicate" -> new Object[] {adrenalineHealthThresholdPercent()};
            case "attack" -> new Object[] {adrenalineAttackPercent(t)};
            case "reload" -> new Object[] {adrenalineReloadPercent(t)};
            case "heal" -> new Object[] {secondChanceHeal(t)};
            case "cooldown" -> cooldownArgs(skillId, t);
            case "effect" -> likeACatEffectMinutes(t);
            case "crit" -> new Object[] {skullCrusherCritMultiplier(t)};
            case "count" -> countArgs(skillId, t);
            case "speed" -> speedArgs(skillId, t);
            case "rate" -> new Object[] {ratePercent(skillId, t)};
            case "capacity" -> new Object[] {capacityBonus(skillId, t)};
            case "hs_damage" -> new Object[] {deadEyeHeadshotPercent(skillId)};
            case "double" -> new Object[] {motherLodeDouble(t)};
            case "tripple" -> new Object[] {motherLodeTriple(t)};
            case "delay" -> new Object[] {resistDelaySeconds(t)};
            case "resist" -> new Object[] {resistPercent(t)};
            case "med_effect" -> new Object[] {medEffectPercent(t)};
            case "med_delay" -> new Object[] {medDelaySeconds(t)};
            case "info" -> ironBuddyHp(t);
            case "jamming" -> new Object[] {carefulGunnerJamPercent(t)};
            case "unjamming" -> new Object[] {carefulGunnerUnjamPercent(t)};
            case "durability" -> new Object[] {
                skillId.startsWith("lucky_shooter_") ? luckyShooterWearPercent(t) : invPercentTier(t)
            };
            case "repair" -> new Object[] {repairPercent(t)};
            case "plank" ->
                    new Object[] {(int) (MiningSkillService.lumberjackPlankChanceForTier(t) * 100)};
            case "stick" ->
                    new Object[] {(int) (MiningSkillService.lumberjackStickChanceForTier(t) * 100)};
            case "ammo", "rewards", "meds", "orbs", "perkBook", "explosives", "flares" ->
                    bartenderArgs(suffix, t);
            default -> new Object[0];
        };
    }

    private static Object[] countArgs(String skillId, int t) {
        if (skillId.startsWith("bone_grinder")) {
            int yield = switch (t) {
                case 3 -> 8;
                case 2 -> 5;
                default -> 3;
            };
            return new Object[] {yield};
        }
        if (skillId.startsWith("gunpowder_")) {
            int yield = switch (skillId) {
                case "gunpowder_master" -> 6;
                case "gunpowder_expert" -> 4;
                default -> 2;
            };
            return new Object[] {yield};
        }
        return new Object[0];
    }

    private static Object[] speedArgs(String skillId, int t) {
        if (skillId.startsWith("agility_")) {
            int pct =
                    switch (t) {
                        case 5 -> 35;
                        case 4 -> 28;
                        case 3 -> 20;
                        case 2 -> 10;
                        default -> 5;
                    };
            return new Object[] {pct};
        }
        if (skillId.contains("_quickdraw")) {
            return new Object[] {15};
        }
        int reload = switch (t) {
            case 3 -> 25;
            case 2 -> 15;
            default -> 10;
        };
        return new Object[] {reload};
    }

    private static int ratePercent(String skillId, int t) {
        return switch (t) {
            case 3 -> 15;
            case 2 -> 10;
            default -> 5;
        };
    }

    private static int capacityBonus(String skillId, int t) {
        int fromExt = WeaponExtensionService.magazineBonusForExtensionId(skillId);
        if (fromExt > 0) {
            return fromExt;
        }
        if ("deagle_extended".equals(skillId)) {
            return 3;
        }
        if ("akm_extended".equals(skillId)) {
            return 10;
        }
        if ("aug_extended".equals(skillId) || "aug_extended_mk2".equals(skillId)) {
            return 10;
        }
        return switch (t) {
            case 3 -> 6;
            case 2 -> 4;
            default -> 2;
        };
    }

    private static int deadEyeHeadshotPercent(String skillId) {
        return 25;
    }

    private static int motherLodeDouble(int t) {
        return switch (t) {
            case 5 -> 65;
            case 4 -> 50;
            case 3 -> 35;
            case 2 -> 20;
            default -> 10;
        };
    }

    private static int motherLodeTriple(int t) {
        return switch (t) {
            case 5 -> 25;
            case 4 -> 15;
            default -> 0;
        };
    }

    private static int resistPercent(int t) {
        return switch (t) {
            case 3 -> 45;
            case 2 -> 30;
            default -> 15;
        };
    }

    private static int resistDelaySeconds(int t) {
        return switch (t) {
            case 3 -> 60;
            case 2 -> 40;
            default -> 20;
        };
    }

    private static int medEffectPercent(int t) {
        return switch (t) {
            case 3 -> 30;
            case 2 -> 20;
            default -> 10;
        };
    }

    private static int medDelaySeconds(int t) {
        return switch (t) {
            case 3 -> 60;
            case 2 -> 40;
            default -> 20;
        };
    }

    private static Object[] ironBuddyHp(int t) {
        int hp = switch (t) {
            case 3 -> 100;
            case 2 -> 75;
            default -> 50;
        };
        return new Object[] {hp};
    }

    private static int ironBuddyCooldown(int t) {
        return switch (t) {
            case 3 -> 300;
            case 2 -> 450;
            default -> 600;
        };
    }

    private static int invPercentTier(int t) {
        return switch (t) {
            case 3 -> 20;
            case 2 -> 10;
            default -> 5;
        };
    }

    private static int carefulGunnerJamPercent(int t) {
        return switch (t) {
            case 5 -> 100;
            case 4 -> 70;
            case 3 -> 45;
            case 2 -> 25;
            default -> 10;
        };
    }

    private static int carefulGunnerUnjamPercent(int t) {
        return switch (t) {
            case 5 -> 35;
            case 4 -> 25;
            case 3 -> 15;
            case 2 -> 10;
            default -> 5;
        };
    }

    private static int luckyShooterWearPercent(int t) {
        return switch (t) {
            case 5 -> 55;
            case 4 -> 38;
            case 3 -> 22;
            case 2 -> 12;
            default -> 5;
        };
    }

    private static int repairPercent(int t) {
        return switch (t) {
            case 3 -> 20;
            case 2 -> 10;
            default -> 5;
        };
    }

    private static Object[] bartenderArgs(String suffix, int t) {
        return switch (suffix) {
            case "ammo" -> new Object[] {switch (t) {
                case 5 -> 50;
                case 4 -> 40;
                case 3 -> 30;
                case 2 -> 20;
                default -> 10;
            }};
            case "rewards" -> new Object[] {t >= 4 ? 2 : 1};
            case "meds", "orbs", "perkBook", "explosives" -> new Object[] {1};
            case "flares" -> new Object[] {2};
            default -> new Object[0];
        };
    }

    public static String formatPercent(int value) {
        return String.format(Locale.ROOT, "%d", value);
    }

    private static int strongMusclesDisplay(int t) {
        return switch (t) {
            case 5 -> 10;
            case 4 -> 8;
            case 3 -> 6;
            case 2 -> 4;
            default -> 2;
        };
    }

    private static Object[] chanceArgs(String skillId, int t) {
        if (skillId.startsWith("skull_crusher_")) {
            return new Object[] {5, skullCrusherCritMultiplier(t)};
        }
        if (skillId.startsWith("well_fed_")) {
            int pct =
                    switch (t) {
                        case 5 -> 90;
                        case 4 -> 80;
                        case 3 -> 70;
                        case 2 -> 55;
                        default -> 40;
                    };
            return new Object[] {pct};
        }
        return new Object[0];
    }

    private static int skullCrusherCritMultiplier(int t) {
        return t > 0 ? t + 1 : 2;
    }

    private static int adrenalineHealthThresholdPercent() {
        return 25;
    }

    private static int adrenalineAttackPercent(int t) {
        return switch (t) {
            case 5 -> 65;
            case 4 -> 55;
            case 3 -> 50;
            case 2 -> 30;
            default -> 15;
        };
    }

    private static int adrenalineReloadPercent(int t) {
        return switch (t) {
            case 5 -> 35;
            case 4 -> 28;
            case 3 -> 20;
            case 2 -> 10;
            default -> 5;
        };
    }

    private static int secondChanceHeal(int t) {
        return switch (t) {
            case 5 -> 30;
            case 4 -> 25;
            case 3 -> 20;
            case 2 -> 15;
            default -> 10;
        };
    }

    private static Object[] secondChanceCooldownArgs(int t) {
        int minutes =
                switch (t) {
                    case 5 -> 6;
                    case 4 -> 7;
                    case 3 -> 9;
                    case 2 -> 12;
                    default -> 15;
                };
        return new Object[] {minutes + " 分钟"};
    }

    private static Object[] cooldownArgs(String skillId, int t) {
        if (skillId.startsWith("like_a_cat_")) {
            return likeACatCooldownMinutes(t);
        }
        if (skillId.startsWith("second_chance_")) {
            return secondChanceCooldownArgs(t);
        }
        return new Object[] {ironBuddyCooldown(t)};
    }

    private static Object[] likeACatCooldownMinutes(int t) {
        return new Object[] {"5 分钟"};
    }

    private static Object[] likeACatEffectMinutes(int t) {
        int minutes =
                switch (t) {
                    case 5 -> 10;
                    case 4 -> 8;
                    case 3 -> 7;
                    case 2 -> 4;
                    default -> 2;
                };
        return new Object[] {minutes + " 分钟"};
    }
}
