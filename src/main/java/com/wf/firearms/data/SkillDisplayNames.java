package com.wf.firearms.data;

import com.wf.firearms.gameplay.MiningSkillService;
import com.wf.firearms.gameplay.WeaponExtensionDescriptions;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/** 技能/武器显示名（客户端与服务端共用）。 */
public final class SkillDisplayNames {
    private static final String[] DESCRIPTION_SUFFIXES = {
            "0", "1", "2",
            "count", "speed", "rate", "capacity", "ammo", "bolt",
            "delay", "resist", "med_effect", "med_delay",
            "double", "tripple",
            "info", "cooldown",
            "damage", "chance", "effect", "predicate", "attack", "reload",
            "rewards", "meds", "orbs", "perkBook", "explosives", "flares",
            "jamming", "unjamming", "durability", "repair",
            "plank", "stick", "hs_damage"
    };

    private static final Map<String, String> TIER = Map.ofEntries(
            Map.entry("i", " I"),
            Map.entry("ii", " II"),
            Map.entry("iii", " III"),
            Map.entry("iv", " IV"),
            Map.entry("v", " V"),
            Map.entry("novice", " I"),
            Map.entry("expert", " II"),
            Map.entry("master", " III"));

    private static final Map<String, String> EXT_SUFFIX_CN = Map.ofEntries(
            Map.entry("quickdraw", "快手"),
            Map.entry("extended", "加长弹匣"),
            Map.entry("extended_mk2", "加长弹匣 II"),
            Map.entry("suppressor", "消音器"),
            Map.entry("reliable", "坚实可靠"),
            Map.entry("vertical", "垂直握把"),
            Map.entry("vertical_grip", "垂直握把"),
            Map.entry("red_dot", "红点瞄准镜"),
            Map.entry("compensator", "补偿器"),
            Map.entry("cheekpad", "托腮板"),
            Map.entry("scope", "瞄准镜"),
            Map.entry("penetrator", "穿甲弹"),
            Map.entry("fast_hands", "快手 II"),
            Map.entry("dead_eye", "死神之眼"),
            Map.entry("overloaded", "过载"),
            Map.entry("heavy_bullets", "重弹"),
            Map.entry("tough_spring", "强化弹簧"),
            Map.entry("every_bullet_counts", "弹尽粮绝"),
            Map.entry("carbon_barrel", "碳纤维枪管"),
            Map.entry("killing_spree", "杀戮狂欢"),
            Map.entry("soul_taker", "灵魂杀手"),
            Map.entry("scalding_hot", "炙热滚烫"),
            Map.entry("extended_barrel", "加长枪管"),
            Map.entry("bullet_loops", "子弹环"),
            Map.entry("adaptive_chambering", "自适应枪膛"),
            Map.entry("hunter", "猎手"),
            Map.entry("cold_blooded", "冷血"),
            Map.entry("finisher", "终结者"),
            Map.entry("cruel", "残忍"),
            Map.entry("veteran_hunter", "老练猎手"),
            Map.entry("gear_grinder", "齿轮研磨"),
            Map.entry("demolition_expert", "爆破专家"),
            Map.entry("better_cartridge", "改良弹药"),
            Map.entry("light_trigger", "轻扳机"),
            Map.entry("repeater", "连射器"),
            Map.entry("quiver", "箭袋"),
            Map.entry("tough_bowstring", "强化弓弦"),
            Map.entry("poisoned_bolts", "毒箭"),
            Map.entry("heavy_bolts", "重箭"),
            Map.entry("choke", "收束器"),
            Map.entry("pump_in_action", "子弹上膛"),
            Map.entry("never_give_up", "永无止境"),
            Map.entry("brutal_cannon", "无情重炮"),
            Map.entry("quick_shift", "快速转移"),
            Map.entry("deft_reload", "游刃有余"),
            Map.entry("precision_care", "精密保养"),
            Map.entry("hot_hands", "手感火热"),
            Map.entry("back_three", "背身三枪"),
            Map.entry("ace_of_hearts", "红心王牌"),
            Map.entry("commando", "突击队"),
            Map.entry("blazing_pellets", "炽热弹丸"),
            Map.entry("cannon_blast", "铅弹之风"),
            Map.entry("bullet_recycle", "子弹回收"),
            Map.entry("water_cooling", "水冷科技"),
            Map.entry("careful_maintenance", "细心保养"),
            Map.entry("barrel_overheat", "枪管过热"),
            Map.entry("kinetic_focus", "动能集中"),
            Map.entry("heavy_kit", "沉重配件"),
            Map.entry("explosive_shot", "爆炸射击"),
            Map.entry("bandolier", "子弹袋"),
            Map.entry("harvester", "收割者"),
            Map.entry("atrocity", "暴虐"),
            Map.entry("shrapnel", "破片弹"),
            Map.entry("glory_kill", "荣耀击杀"),
            Map.entry("close_quarters", "短枪肉搏"),
            Map.entry("armor_bane", "重装克星"),
            Map.entry("unarmored_loot", "剥取"),
            Map.entry("quick_mover", "快速移动"),
            Map.entry("power_knockback", "强力击退"),
            Map.entry("rapid_fire", "极速射击"),
            Map.entry("heavy_rounds", "重尖弹"));

    /**
     * 由脚本从模板枪复制的扩展天赋：语言包只写了模板枪时，从此映射继承说明。
     * 见 {@code tools/clone_weapon_extensions.py}。
     */
    private static final Map<String, String> EXTENSION_LANG_TEMPLATE = Map.ofEntries(
            Map.entry("glock", "m1911"),
            Map.entry("p90", "vector"),
            Map.entry("uzi", "vector"),
            Map.entry("gatling", "vector"),
            Map.entry("type_81", "hk416"),
            Map.entry("pkm", "hk416"),
            Map.entry("m249", "hk416"));

    private SkillDisplayNames() {}

    public static String resolveTitle(String id) {
        if (id == null || id.isEmpty()) {
            return "?";
        }
        Component c = Component.translatable("skill.gunsrpg." + id + ".title");
        String s = c.getString();
        if (!s.startsWith("skill.gunsrpg.")) {
            return s;
        }
        if (id.endsWith("_assembly")) {
            return WeaponMapping.assemblyTitleLine(id);
        }
        return fallbackTitle(id);
    }

    public static String resolveDescription(String id, String suffix) {
        if ("rate".equals(suffix) && WeaponExtensionDescriptions.usesDynamicFireIntervalDescription(id)) {
            return "";
        }
        if ("rate".equals(suffix) && id != null && id.endsWith("_gear_grinder")) {
            return "";
        }
        Object[] args = SkillDescriptionValues.argsFor(id, suffix);
        Component c = args.length > 0
                ? Component.translatable("skill.gunsrpg." + id + ".description." + suffix, args)
                : Component.translatable("skill.gunsrpg." + id + ".description." + suffix);
        String s = c.getString();
        if (!s.startsWith("skill.gunsrpg.")) {
            return s;
        }
        return "";
    }

    public static List<String> resolveDescriptionLines(String id) {
        List<String> lines = new ArrayList<>();
        if (id != null && id.endsWith("_pump_in_action")) {
            lines.addAll(WeaponExtensionDescriptions.pumpInActionLines(id));
            if (!lines.isEmpty()) {
                return lines;
            }
        }
        if (id != null && id.endsWith("_deft_reload")) {
            lines.addAll(WeaponExtensionDescriptions.deftReloadLines(id));
            if (!lines.isEmpty()) {
                return lines;
            }
        }
        if (id != null) {
            List<String> fire = WeaponExtensionDescriptions.fireIntervalLines(id);
            if (!fire.isEmpty()) {
                return fire;
            }
            List<String> gear = WeaponExtensionDescriptions.gearGrinderLines(id);
            if (!gear.isEmpty()) {
                return gear;
            }
        }
        if (id != null && id.endsWith("_red_dot")) {
            lines.add("减少武器弹道散布 15%");
            return lines;
        }
        if (id != null && id.endsWith("_scope")) {
            lines.add("减少弹道散布 25%，爆头伤害 +10%");
            return lines;
        }
        lines.addAll(collectLangDescriptionLines(id));
        if (lines.isEmpty()) {
            lines.addAll(descriptionFromTemplateWeapon(id));
        }
        if (lines.isEmpty()) {
            lines.addAll(genericExtensionDescriptionBySuffix(id));
        }
        if (lines.isEmpty()) {
            String fb = clientDescriptionFallback(id);
            if (!fb.isEmpty()) {
                lines.add(fb);
            }
        }
        return lines;
    }

    private static List<String> collectLangDescriptionLines(String id) {
        List<String> lines = new ArrayList<>();
        if (id == null || id.isEmpty()) {
            return lines;
        }
        for (String suffix : DESCRIPTION_SUFFIXES) {
            String line = resolveDescription(id, suffix);
            if (!line.isEmpty()) {
                lines.add(line);
            }
        }
        return lines;
    }

    /** 克隆枪扩展：从模板枪同名后缀天赋读取语言包说明。 */
    private static List<String> descriptionFromTemplateWeapon(String id) {
        Optional<ExtensionIdParts> parts = parseExtensionId(id);
        if (parts.isEmpty()) {
            return List.of();
        }
        String templateWeapon = EXTENSION_LANG_TEMPLATE.get(parts.get().weaponKey());
        if (templateWeapon == null) {
            return List.of();
        }
        String templateSkillId = templateWeapon + "_" + parts.get().extSuffix();
        return collectLangDescriptionLines(templateSkillId);
    }

    private record ExtensionIdParts(String weaponKey, String extSuffix) {}

    private static Optional<ExtensionIdParts> parseExtensionId(String id) {
        if (id == null || id.isEmpty() || id.endsWith("_assembly")) {
            return Optional.empty();
        }
        List<String> extKeys = new ArrayList<>(EXT_SUFFIX_CN.keySet());
        extKeys.sort(Comparator.comparingInt(String::length).reversed());
        for (String extKey : extKeys) {
            String marker = "_" + extKey;
            if (id.endsWith(marker)) {
                String weaponKey = id.substring(0, id.length() - marker.length());
                if (!weaponKey.isEmpty()) {
                    return Optional.of(new ExtensionIdParts(weaponKey, extKey));
                }
            }
        }
        return Optional.empty();
    }

    /** 无语言包、无模板时的通用扩展说明（按后缀，全枪共用）。 */
    private static List<String> genericExtensionDescriptionBySuffix(String id) {
        Optional<ExtensionIdParts> parts = parseExtensionId(id);
        if (parts.isEmpty()) {
            return List.of();
        }
        return switch (parts.get().extSuffix()) {
            case "carbon_barrel" -> List.of("减少水平和垂直方向后坐力");
            case "every_bullet_counts" -> List.of("弹匣中最后一发子弹额外造成 200% 伤害");
            case "heavy_bullets" ->
                    List.of(
                            "击中时有 35% 几率造成以下效果:",
                            "缓慢 II，持续 5 秒",
                            "虚弱 I，持续 5 秒");
            case "killing_spree" -> List.of("每击杀一个目标提供 +10% 伤害，持续一小段时间 (最多叠加至 30%)");
            case "overloaded" -> List.of("每次射击有 10% 的几率不消耗子弹");
            case "compensator" -> List.of("减少武器后坐力");
            case "finisher" -> List.of("对血量 50% 以下的目标造成额外 25% 伤害");
            case "cruel" -> List.of("对生命值低于 50% 的单位额外造成 30% 伤害");
            case "penetrator" -> List.of("子弹能够穿透多个目标");
            case "cheekpad" -> List.of("减少武器后坐力");
            case "vertical", "vertical_grip" -> List.of("减少垂直方向后坐力");
            case "adaptive_chambering" -> List.of("解锁全自动射击模式");
            case "hot_hands" ->
                    List.of(
                            "连续快速射击时，下一发伤害每层 +10%（间隔随当前射速）；加长弹匣叠满时最后一发可达 +100%");
            case "back_three" -> List.of("停火 5 秒后，下三次射击无后坐力并额外 +20% 伤害");
            case "hunter" -> List.of("对动物额外造成伤害");
            case "cold_blooded" -> List.of("对满血目标额外造成伤害");
            case "light_trigger" -> List.of("缩短射击间隔");
            case "fast_hands" -> List.of("大幅缩短射击间隔");
            case "repeater" -> List.of("缩短半自动射击间隔");
            case "demolition_expert" -> List.of("榴弹/爆炸物相关强化");
            case "better_cartridge" -> List.of("提升弹药效能");
            case "soul_taker" -> List.of("对亡灵生物额外造成 25% 伤害");
            case "scalding_hot" -> List.of("对燃烧中的敌人额外造成 25% 伤害");
            case "choke" -> List.of("减少子弹散布");
            case "never_give_up" -> List.of("每次击杀提供抗性提升 I，持续 5 秒");
            case "brutal_cannon" -> List.of("对 8 格以内的敌人额外造成 50% 伤害");
            case "quick_shift" -> List.of("持枪时移动速度 +20%");
            case "precision_care" -> List.of("武器耐久损耗 -15%，卡弹概率 -25%");
            case "cannon_blast" -> List.of("一次发射两根枪管内的子弹");
            case "bullet_recycle" -> List.of("有 15% 概率射击时不消耗子弹");
            case "water_cooling" -> List.of("连续射击时枪管过热速度降低 20%");
            case "careful_maintenance" -> List.of("卡弹率降低 30%");
            case "barrel_overheat" -> List.of("枪管热度超过 50% 时，额外造成 20% 伤害");
            case "kinetic_focus" -> List.of("子弹基础伤害 +2");
            case "heavy_kit" -> List.of("额外降低 20% 移动速度；水平与垂直后坐力各降低 20%");
            case "explosive_shot" -> List.of("每次射击发射 3 颗子弹", "后坐力 +30%");
            case "bandolier" -> List.of("弹匣容量 +2", "换弹时间减少 40%");
            case "reliable" -> List.of("武器耐久损耗 -15%", "卡弹概率 -20%");
            case "harvester" -> List.of("击杀时有 15% 概率使战利品翻倍");
            case "veteran_hunter" -> List.of("每过 5 秒，下次射击额外造成 300% 伤害");
            case "atrocity" ->
                    List.of("对 4 格以内敌人额外造成 200% 伤害", "对 8 格以内敌人额外造成 100% 伤害");
            case "shrapnel" -> List.of("额外发射 2 颗弹丸");
            case "glory_kill" -> List.of("每次击杀获得伤害吸收 I（30 秒）并恢复 2 点生命");
            case "close_quarters" -> List.of("对 8 格以内敌人额外造成 60% 伤害");
            case "armor_bane" -> List.of("对护甲值 ≥10 的敌人额外造成 30% 伤害");
            case "unarmored_loot" -> List.of("击杀无护甲敌人时掉落物翻倍");
            case "quick_mover" -> List.of("携带该枪时移动速度 +30%");
            case "blazing_pellets" -> List.of("点燃敌人");
            case "power_knockback" -> List.of("命中时对目标造成击退");
            case "rapid_fire" -> List.of("射击间隔缩短 0.2 秒");
            case "heavy_rounds" -> List.of("子弹基础伤害 +3");
            default -> List.of();
        };
    }

    /** @deprecated 使用 {@link #resolveDescriptionLines} */
    public static String resolveDescriptionLine(String id) {
        List<String> lines = resolveDescriptionLines(id);
        return lines.isEmpty() ? "" : lines.get(0);
    }

    /** 客户端兜底中文（不依赖语言包是否加载）。 */
    public static String clientDescriptionFallback(String id) {
        if (id.startsWith("bone_grinder")) {
            int yield =
                    switch (id) {
                        case "bone_grinder_iii" -> 8;
                        case "bone_grinder_ii" -> 5;
                        default -> 3;
                    };
            return String.format(Locale.ROOT, "允许在工作台用骨头制作骨粉，单次产出 %d 个", yield);
        }
        if (id.startsWith("mother_lode_")) {
            int t = SkillDescriptionValues.tier(id);
            int d = SkillDescriptionValues.argsFor(id, "double")[0] instanceof Integer i ? i : 10;
            return String.format(Locale.ROOT, "挖矿时有 %d%% 几率获得 2 倍掉落", d);
        }
        if (id.startsWith("bleeding_resistance_") || id.startsWith("fracture_resistance_")
                || id.startsWith("poison_resistance_") || id.startsWith("infection_resistance_")) {
            int t = SkillDescriptionValues.tier(id);
            return String.format(
                    Locale.ROOT,
                    "降低对应 Debuff 触发率 %d%%，恶化间隔 +%d 秒，提升相关药品疗效",
                    SkillDescriptionValues.argsFor(id, "resist")[0],
                    SkillDescriptionValues.argsFor(id, "delay")[0]);
        }
        if (id.startsWith("agility_")) {
            return String.format(
                    Locale.ROOT,
                    "增加 %s%% 移动速度",
                    SkillDescriptionValues.argsFor(id, "speed")[0]);
        }
        if (id.startsWith("careful_gunner_")) {
            int jam = SkillDescriptionValues.argsFor(id, "jamming")[0] instanceof Integer i ? i : 10;
            int unjam = SkillDescriptionValues.argsFor(id, "unjamming")[0] instanceof Integer i ? i : 5;
            return String.format(
                    Locale.ROOT, "减少武器卡弹几率 %d%%；减少解除卡弹所需时间 %d%%", jam, unjam);
        }
        if (id.startsWith("lucky_shooter_")) {
            int pct = SkillDescriptionValues.argsFor(id, "durability")[0] instanceof Integer i ? i : 5;
            return String.format(Locale.ROOT, "减少武器射击时的耗损 %d%%", pct);
        }
        if (id.startsWith("ammo_smithing_mastery_")) {
            return switch (id) {
                case "ammo_smithing_mastery_i" -> "标准弹合成额外产出 +1 发";
                case "ammo_smithing_mastery_ii" -> "马格南弹合成 25% 几率额外 +1 发";
                case "ammo_smithing_mastery_iii" -> "标准弹合成再额外 +1 发（累计 +2）";
                case "ammo_smithing_mastery_iv" -> "马格南弹额外 +1 发几率提升至 75%";
                case "ammo_smithing_mastery_v" -> "标准弹再 +1（累计 +3）；马格南弹稳定额外 +1 发";
                default -> "";
            };
        }
        if (id.startsWith("lumberjack_")) {
            int t = SkillDescriptionValues.tier(id);
            int plank = (int) (MiningSkillService.lumberjackPlankChanceForTier(t) * 100);
            int stick = (int) (MiningSkillService.lumberjackStickChanceForTier(t) * 100);
            return String.format(Locale.ROOT, "砍原木时 %d%% 额外木板、%d%% 额外 2 根木棍", plank, stick);
        }
        if (id.startsWith("grave_digger_")) {
            int pct = (int) (MiningSkillService.graveDiggerSpeedBonus(SkillDescriptionValues.tier(id)) * 100);
            return String.format(Locale.ROOT, "挖掘沙砾速度 +%d%%", pct);
        }
        if (id.startsWith("heavy_pickaxe_")) {
            int pct = (int) (MiningSkillService.heavyPickaxeSpeedBonus(SkillDescriptionValues.tier(id)) * 100);
            return String.format(Locale.ROOT, "使用镐类挖掘速度 +%d%%", pct);
        }
        if (id.startsWith("sharp_axe_")) {
            int pct = (int) (MiningSkillService.sharpAxeSpeedBonus(SkillDescriptionValues.tier(id)) * 100);
            return String.format(Locale.ROOT, "使用斧类挖掘速度 +%d%%", pct);
        }
        if (id.startsWith("acrobatics_")) {
            int pct = SkillDescriptionValues.tier(id) * 12;
            return String.format(Locale.ROOT, "摔落距离减免约 %d%%", pct);
        }
        if ("local_chef".equals(id)) {
            return "烹饪台：培根汉堡、吮指全鸡、鸡蛋沙拉、炸鱼薯条、水果沙拉、花园大乱炖；枪械台：烹饪台";
        }
        if ("master_chef".equals(id)) {
            return "烹饪台：豪华大餐、全肉盛宴XXL、兔兔奶油汤、牧羊人的饼、巧克力苹果馅饼、生圈饼、寿司卷";
        }
        if ("medical_station".equals(id)) {
            return "枪械台合成医疗站方块；放置后右键打开医疗合成（需后续郎中/药剂师技能解锁配方）";
        }
        if ("medic".equals(id)) {
            return "医疗站配方：止血绷带×2、石膏夹板、止血剂";
        }
        if ("doctor".equals(id)) {
            return "医疗站配方：抗感染疫苗、解毒片×2";
        }
        if (id.startsWith("pharmacist_")) {
            int tier = SkillDescriptionValues.tier(id);
            return switch (tier) {
                case 1 -> "医疗站：镇痛片";
                case 2 -> "医疗站：类固醇";
                case 3 -> "医疗站：肾上腺素";
                case 4 -> "医疗站：止痛药";
                case 5 -> "医疗站：吗啡";
                default -> "药剂师进阶";
            };
        }
        if (id.equals("efficient_meds")) {
            return "使用任意医疗品后额外恢复约 6 点生命 + 已损失生命 2%";
        }
        if (id.equals("grenades") || id.equals("grenadier") || id.endsWith("_grenades")) {
            return "手雷相关（本包：投掷物尚未实装，配方与效果后续版本加入）";
        }
        if (id.contains("grenade_launcher") || id.contains("rocket_launcher")) {
            return "榴弹/火箭筒（本包：武器与弹药尚未实装）";
        }
        if (id.contains("_quickdraw")) {
            return "加快 15% 装弹速度";
        }
        if (id.endsWith("_dead_eye")) {
            return "增加 25% 爆头伤害";
        }
        if ("akm_scalding_hot".equals(id)) {
            return "对燃烧中的敌人额外造成 25% 伤害";
        }
        if ("aug_soul_taker".equals(id)) {
            return "对亡灵生物额外造成 25% 伤害";
        }
        if (id.endsWith("_extended") || id.endsWith("_extended_mk2")) {
            int cap = SkillDescriptionValues.argsFor(id, "capacity")[0] instanceof Integer i ? i : 10;
            return String.format(Locale.ROOT, "+%d 弹匣容量", cap);
        }
        if (id.endsWith("_suppressor")) {
            return "减少武器发射时的声音";
        }
        return DESC_FALLBACK.getOrDefault(id, "");
    }

    private static final Map<String, String> DESC_FALLBACK = Map.ofEntries(
            Map.entry("gunpowder_novice", "允许制作火药，单次产出 2 个"),
            Map.entry("gunpowder_expert", "允许制作火药，单次产出 4 个"),
            Map.entry("gunpowder_master", "允许制作火药，单次产出 6 个"),
            Map.entry("wooden_ammo_smith", "允许制作弹壳及木制子弹"),
            Map.entry("gun_parts_smith", "允许制作枪械零件"),
            Map.entry("mineralogist", "识别矿石时显示额外信息（本包：待接）"),
            Map.entry("repair_man_i", "提高维修站修理后保留的耐久上限比例"));

    public static String fallbackTitle(String id) {
        String base = id;
        String tier = "";
        if (base.endsWith("_assembly")) {
            base = base.substring(0, base.length() - "_assembly".length());
            return formatWords(base);
        }
        int us = base.lastIndexOf('_');
        if (us > 0) {
            String suffix = base.substring(us + 1).toLowerCase(Locale.ROOT);
            if (TIER.containsKey(suffix)) {
                tier = TIER.get(suffix);
                base = base.substring(0, us);
            }
        }
        var extKeys = new ArrayList<>(EXT_SUFFIX_CN.keySet());
        extKeys.sort((a, b) -> Integer.compare(b.length(), a.length()));
        for (String extKey : extKeys) {
            String marker = "_" + extKey;
            if (id.endsWith(marker)) {
                String weaponKey = id.substring(0, id.length() - marker.length());
                return WeaponMapping.displayNameForWeaponKey(weaponKey) + " · " + EXT_SUFFIX_CN.get(extKey);
            }
        }
        return formatWords(base) + tier;
    }

    private static String formatWords(String base) {
        String[] parts = base.split("_");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) {
                continue;
            }
            if (!sb.isEmpty()) {
                sb.append(' ');
            }
            sb.append(Character.toUpperCase(p.charAt(0)));
            if (p.length() > 1) {
                sb.append(p.substring(1));
            }
        }
        return sb.toString();
    }
}
