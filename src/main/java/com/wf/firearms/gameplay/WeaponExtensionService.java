package com.wf.firearms.gameplay;

import com.wf.firearms.combat.FirearmCombat;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.SkillDatabase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 武器扩展技能对枪械数值的叠加（对齐原版 Modifiers）。 */
public final class WeaponExtensionService {
    private static final Map<String, Integer> MAG_BONUS = new HashMap<>();

    static {
        mag("m1911_extended", 6);
        mag("r45_extended", 3);
        mag("deagle_extended", 3);
        mag("ump45_extended", 15);
        mag("thompson_extended", 20);
        mag("vector_extended", 16);
        mag("vector_extended_mk2", 17);
        mag("s1897_extended", 3);
        mag("s12k_extended_drum", 7);
        mag("akm_extended", 10);
        mag("hk416_extended", 10);
        mag("hk416_extended_mk2", 10);
        mag("aug_extended", 10);
        mag("aug_extended_mk2", 10);
        mag("fn_fal_extended", 10);
        mag("glock_extended", 6);
        mag("p90_extended", 16);
        mag("p90_extended_mk2", 17);
        mag("uzi_extended", 16);
        mag("uzi_extended_mk2", 17);
        mag("type_81_extended", 10);
        mag("type_81_extended_mk2", 10);
        mag("pkm_extended", 10);
        mag("pkm_extended_mk2", 10);
        mag("m249_extended", 25);
        mag("m249_extended_mk2", 25);
        mag("kar98k_bandolier", 2);
        mag("winchester_bandolier", 2);
        mag("sks_extended", 10);
        mag("spr15_extended", 8);
        mag("vss_extended", 10);
        mag("mk14ebr_extended", 10);
        mag("mk14ebr_extended_mk2", 10);
        mag("winchester_extended", 6);
        mag("awm_extended", 3);
        mag("m95_extended", 3);
        mag("s1897_extended", 3);
        mag("s12k_extended_drum", 7);
        mag("grenade_launcher_extended", 2);
    }

    private WeaponExtensionService() {}

    /** 扩展天赋 ID（如 glock_extended）对应的弹匣容量加成，供 UI 说明占位符使用。 */
    public static int magazineBonusForExtensionId(String extensionSkillId) {
        if (extensionSkillId == null || extensionSkillId.isEmpty()) {
            return 0;
        }
        return MAG_BONUS.getOrDefault(extensionSkillId, 0);
    }

    public static WeaponExtensionStats stats(Player player, String weaponKey) {
        if (player == null || weaponKey == null || weaponKey.isEmpty()) {
            return WeaponExtensionStats.NONE;
        }
        WeaponExtensionStats.Builder b = WeaponExtensionStats.builder();
        for (String extId : unlockedExtensions(player, weaponKey)) {
            apply(b, extId);
        }
        return b.build();
    }

    public static int magazineCapacity(Player player, String weaponKey, int baseMag) {
        return Math.max(1, baseMag + stats(player, weaponKey).magBonus());
    }

    public static double reloadTimeMultiplier(Player player, String weaponKey) {
        return stats(player, weaponKey).reloadTimeMult();
    }

    public static double damageMultiplier(Player player, String weaponKey) {
        return stats(player, weaponKey).damageMult();
    }

    public static double spreadMultiplier(Player player, String weaponKey) {
        return stats(player, weaponKey).spreadMult();
    }

    public static double recoilMultiplier(Player player, String weaponKey) {
        return stats(player, weaponKey).recoilMult();
    }

    public static double jamMultiplier(Player player, String weaponKey) {
        return stats(player, weaponKey).jamMult();
    }

    public static boolean isSilenced(Player player, String weaponKey) {
        return stats(player, weaponKey).silenced();
    }

    public static double headshotBonusMultiplier(Player player, String weaponKey) {
        return stats(player, weaponKey).headshotBonusMult();
    }

    public static int fireIntervalDelta(Player player, String weaponKey) {
        return stats(player, weaponKey).fireIntervalDelta();
    }

    /** 含射击模式等运行时条件的间隔修正（TaCZ / 连发判定用）。 */
    public static int contextualFireIntervalDelta(Player player, ItemStack gun, FirearmSpec spec) {
        int delta = stats(player, spec.weaponKey()).fireIntervalDelta();
        if ("mk14ebr".equals(spec.weaponKey())
                && WeaponExtensionIds.isUnlocked(player, "mk14ebr", "tough_spring")) {
            boolean auto = gun != null && !gun.isEmpty() && FirearmCombat.isAutomaticMode(gun, spec);
            delta += auto ? -1 : -3;
        }
        return delta;
    }

    public static double fireIntervalMultiplier(Player player, String weaponKey) {
        return stats(player, weaponKey).fireIntervalMult();
    }

    public static boolean everyBulletCounts(Player player, String weaponKey) {
        return stats(player, weaponKey).everyBulletCounts();
    }

    public static boolean heavyBullets(Player player, String weaponKey) {
        return stats(player, weaponKey).heavyBullets();
    }

    /** 穿甲弹等扩展：额外可穿透实体数（叠加到 TaCZ bullet.pierce）。 */
    public static int pierceBonus(Player player, String weaponKey) {
        return stats(player, weaponKey).pierceBonus();
    }

    /** 对燃烧目标、亡灵等情境额外伤害倍率（乘算）。 */
    public static float contextualDamageMultiplier(Player player, String weaponKey, net.minecraft.world.entity.LivingEntity target) {
        if (player == null || weaponKey == null || target == null) {
            return 1.0f;
        }
        float mult = 1.0f;
        WeaponExtensionStats stats = stats(player, weaponKey);
        if (stats.scaldingHot() && (target.isOnFire() || target.getRemainingFireTicks() > 0)) {
            mult *= 1.25f;
        }
        if (PlayerFirearmsData.isUnlocked(player, "aug_soul_taker")
                && weaponKey.equals("aug")
                && target.getMobType() == net.minecraft.world.entity.MobType.UNDEAD) {
            mult *= 1.25f;
        }
        return mult;
    }

    private static List<String> unlockedExtensions(Player player, String weaponKey) {
        String assembly = weaponKey + "_assembly";
        return SkillDatabase.getNode(assembly)
                .map(
                        n ->
                                n.getExtensions().stream()
                                        .filter(id -> PlayerFirearmsData.isUnlocked(player, id))
                                        .toList())
                .orElse(List.of());
    }

    private static void apply(WeaponExtensionStats.Builder b, String extId) {
        Integer mag = MAG_BONUS.get(extId);
        if (mag != null) {
            b.addMag(mag);
        }
        if (extId.endsWith("_suppressor")) {
            b.mulNoise(0.2).setSilenced(true);
        }
        if (extId.endsWith("_quickdraw")) {
            b.mulReload(0.85);
        }
        if (extId.endsWith("_tough_spring")) {
            if (!"mk14ebr_tough_spring".equals(extId)) {
                b.addFireInterval("deagle_tough_spring".equals(extId) ? -5 : -1);
            }
            b.mulJam(0.9);
        }
        if (extId.endsWith("_rapid_fire")) {
            b.addFireInterval(-4);
        }
        if (extId.endsWith("_heavy_rounds")) {
            b.addPelletDamage(3f);
        }
        if (extId.contains("_vertical")) {
            b.mulSpread(0.7);
        }
        if (extId.endsWith("_compensator")) {
            b.mulRecoil(0.8);
        }
        if (extId.endsWith("_carbon_barrel")) {
            b.mulSpread(0.7);
        }
        if (extId.endsWith("_cheekpad")) {
            b.mulSpread(0.75);
        }
        if (extId.endsWith("_bullet_loops") || extId.endsWith("_bandolier")) {
            b.mulReload(extId.endsWith("_bandolier") ? 0.6 : 0.65);
        }
        if (extId.endsWith("_explosive_shot")) {
            b.mulRecoil(1.3);
        }
        if (extId.endsWith("_fast_hands")) {
            b.mulFireInterval(0.7);
            b.mulReload(0.7);
        }
        if (extId.endsWith("_dead_eye")) {
            b.addHeadshot(0.25);
        }
        if (extId.endsWith("_penetrator")) {
            b.addPierce(1);
        }
        if (extId.endsWith("_heavy_bullets")) {
            b.setHeavyBullets(true);
            b.addDamage(0.1);
        }
        if (extId.endsWith("_every_bullet_counts")) {
            b.setEveryBulletCounts(true);
        }
        if (extId.endsWith("_overloaded")) {
            b.addDamage(0.12);
            b.mulSpread(1.15);
        }
        if (extId.endsWith("_extended_barrel")) {
            if (isShotgunBarrel(extId)) {
                b.addPelletDamage(1f);
            } else {
                b.addDamage(0.08);
                b.mulSpread(0.92);
            }
        }
        if (extId.endsWith("_red_dot")) {
            b.mulSpread(0.85);
        }
        if (extId.endsWith("_scope")) {
            b.mulSpread(0.75);
            b.addHeadshot(0.1);
        }
        if (extId.endsWith("_adaptive_chambering")) {
            b.addFireInterval(-3);
        }
        if (extId.endsWith("_pump_in_action")) {
            b.mulReload(0.85);
        }
        if (extId.endsWith("_deft_reload")) {
            b.mulReload(0.6);
        }
        if (extId.endsWith("_soul_taker") && !"aug_soul_taker".equals(extId)) {
            b.addDamage(0.1);
        }
        if (extId.endsWith("_scalding_hot")) {
            b.setScaldingHot(true);
        }
        if (extId.endsWith("_hunter")) {
            b.addDamage(0.08);
        }
        if (extId.endsWith("_killing_spree")) {
            b.addDamage(0.05);
        }
        if (extId.endsWith("_cold_blooded")) {
            b.addHeadshot(0.12);
        }
        if (extId.endsWith("_repeater")) {
            b.mulFireInterval(0.8);
        }
        if (extId.endsWith("_gear_grinder")) {
            b.mulReload(0.75);
        }
        if (extId.endsWith("_demolition_expert")) {
            b.addDamage(0.2);
        }
        if (extId.endsWith("_better_cartridge")) {
            b.addMag(1);
            b.addDamage(0.05);
        }
        if (extId.endsWith("_blazing_pellets")) {
            b.addDamage(0.1);
        }
        if (extId.endsWith("_choke")) {
            b.mulSpread(0.88);
        }
        if (extId.endsWith("_light_trigger")) {
            b.mulFireInterval(0.9);
        }
        if (extId.endsWith("_ace_of_hearts")) {
            b.addDamage(0.12);
        }
        if (extId.endsWith("_commando")) {
            b.mulReload(0.82);
            b.addDamage(0.05);
        }
        if ("gatling_bullet_recycle".equals(extId)) {
            b.setAmmoReclaimChance(0.15f);
        }
        if ("gatling_water_cooling".equals(extId)) {
            b.setHeatPerShotMult(0.8f);
        }
        if ("gatling_careful_maintenance".equals(extId)) {
            b.mulJam(0.7);
        }
        if ("gatling_kinetic_focus".equals(extId)) {
            b.addPelletDamage(2f);
        }
        if ("gatling_heavy_kit".equals(extId)) {
            b.mulRecoil(0.8);
            b.addHeldMoveSpeed(-0.20f);
        }
    }

    public static float ammoReclaimChance(Player player, String weaponKey) {
        return stats(player, weaponKey).ammoReclaimChance();
    }

    public static float heatPerShotMultiplier(Player player, String weaponKey) {
        return stats(player, weaponKey).heatPerShotMult();
    }

    private static boolean isShotgunBarrel(String extId) {
        return extId.startsWith("db2_")
                || extId.startsWith("s1897_")
                || extId.startsWith("s12k_")
                || extId.startsWith("s686_");
    }

    public static float pelletDamageBonus(Player player, String weaponKey) {
        return stats(player, weaponKey).pelletDamageBonus();
    }

    private static void mag(String id, int bonus) {
        MAG_BONUS.put(id, bonus);
    }

    /** 爆破专家：爆炸半径 +25%。 */
    public static float blastRadiusMultiplier(Player player, String weaponKey) {
        if (player == null || weaponKey == null) {
            return 1f;
        }
        for (String extId : unlockedExtensions(player, weaponKey)) {
            if (extId.endsWith("_demolition_expert")) {
                return 1.25f;
            }
        }
        return 1f;
    }

    /** 高膛压等：抛射初速倍率。 */
    public static float launchSpeedMultiplier(Player player, String weaponKey) {
        if (player == null || weaponKey == null) {
            return 1f;
        }
        for (String extId : unlockedExtensions(player, weaponKey)) {
            if (extId.endsWith("_better_cartridge")) {
                return 1.6f;
            }
        }
        return 1f;
    }

    /** 聚合后的扩展修正。 */
    public static final class WeaponExtensionStats {
        public static final WeaponExtensionStats NONE =
                new WeaponExtensionStats(
                        0,
                        1.0,
                        1.0,
                        1.0,
                        1.0,
                        1.0,
                        false,
                        1.0,
                        0,
                        1.0,
                        false,
                        false,
                        0f,
                        0,
                        false,
                        0f,
                        1f,
                        0f);

        private final int magBonus;
        private final double reloadTimeMult;
        private final double damageMult;
        private final double spreadMult;
        private final double recoilMult;
        private final double jamMult;
        private final boolean silenced;
        private final double headshotBonusMult;
        private final int fireIntervalDelta;
        private final double fireIntervalMult;
        private final boolean everyBulletCounts;
        private final boolean heavyBullets;
        private final float pelletDamageBonus;
        private final int pierceBonus;
        private final boolean scaldingHot;
        private final float ammoReclaimChance;
        private final float heatPerShotMult;
        private final float heldMoveSpeedBonus;

        private WeaponExtensionStats(
                int magBonus,
                double reloadTimeMult,
                double damageMult,
                double spreadMult,
                double recoilMult,
                double jamMult,
                boolean silenced,
                double headshotBonusMult,
                int fireIntervalDelta,
                double fireIntervalMult,
                boolean everyBulletCounts,
                boolean heavyBullets,
                float pelletDamageBonus,
                int pierceBonus,
                boolean scaldingHot,
                float ammoReclaimChance,
                float heatPerShotMult,
                float heldMoveSpeedBonus) {
            this.magBonus = magBonus;
            this.reloadTimeMult = reloadTimeMult;
            this.damageMult = damageMult;
            this.spreadMult = spreadMult;
            this.recoilMult = recoilMult;
            this.jamMult = jamMult;
            this.silenced = silenced;
            this.headshotBonusMult = headshotBonusMult;
            this.fireIntervalDelta = fireIntervalDelta;
            this.fireIntervalMult = fireIntervalMult;
            this.everyBulletCounts = everyBulletCounts;
            this.heavyBullets = heavyBullets;
            this.pelletDamageBonus = pelletDamageBonus;
            this.pierceBonus = pierceBonus;
            this.scaldingHot = scaldingHot;
            this.ammoReclaimChance = ammoReclaimChance;
            this.heatPerShotMult = heatPerShotMult;
            this.heldMoveSpeedBonus = heldMoveSpeedBonus;
        }

        public int magBonus() {
            return magBonus;
        }

        public double reloadTimeMult() {
            return reloadTimeMult;
        }

        public double damageMult() {
            return damageMult;
        }

        public double spreadMult() {
            return spreadMult;
        }

        public double recoilMult() {
            return recoilMult;
        }

        public double jamMult() {
            return jamMult;
        }

        public boolean silenced() {
            return silenced;
        }

        public double headshotBonusMult() {
            return headshotBonusMult;
        }

        public int fireIntervalDelta() {
            return fireIntervalDelta;
        }

        public double fireIntervalMult() {
            return fireIntervalMult;
        }

        public boolean everyBulletCounts() {
            return everyBulletCounts;
        }

        public boolean heavyBullets() {
            return heavyBullets;
        }

        public float pelletDamageBonus() {
            return pelletDamageBonus;
        }

        public int pierceBonus() {
            return pierceBonus;
        }

        public boolean scaldingHot() {
            return scaldingHot;
        }

        public float ammoReclaimChance() {
            return ammoReclaimChance;
        }

        public float heatPerShotMult() {
            return heatPerShotMult;
        }

        public float heldMoveSpeedBonus() {
            return heldMoveSpeedBonus;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private int magBonus;
            private double reloadTimeMult = 1.0;
            private double damageMult = 1.0;
            private double spreadMult = 1.0;
            private double recoilMult = 1.0;
            private double jamMult = 1.0;
            private boolean silenced;
            private double headshotBonusMult = 1.0;
            private int fireIntervalDelta;
            private double fireIntervalMult = 1.0;
            private boolean everyBulletCounts;
            private boolean heavyBullets;
            private float pelletDamageBonus;
            private int pierceBonus;
            private boolean scaldingHot;
            private float ammoReclaimChance;
            private float heatPerShotMult = 1f;
            private float heldMoveSpeedBonus;

            Builder addMag(int v) {
                magBonus += v;
                return this;
            }

            Builder mulReload(double v) {
                reloadTimeMult *= v;
                return this;
            }

            Builder addDamage(double add) {
                damageMult += add;
                return this;
            }

            Builder mulSpread(double v) {
                spreadMult *= v;
                return this;
            }

            Builder mulRecoil(double v) {
                recoilMult *= v;
                return this;
            }

            Builder mulJam(double v) {
                jamMult *= v;
                return this;
            }

            Builder mulNoise(double v) {
                return this;
            }

            Builder setSilenced(boolean s) {
                silenced = s;
                return this;
            }

            Builder addHeadshot(double add) {
                headshotBonusMult += add;
                return this;
            }

            Builder addFireInterval(int delta) {
                fireIntervalDelta += delta;
                return this;
            }

            Builder mulFireInterval(double v) {
                fireIntervalMult *= v;
                return this;
            }

            Builder setEveryBulletCounts(boolean v) {
                everyBulletCounts = v;
                return this;
            }

            Builder setHeavyBullets(boolean v) {
                heavyBullets = v;
                return this;
            }

            Builder addPelletDamage(float add) {
                pelletDamageBonus += add;
                return this;
            }

            Builder addPierce(int add) {
                pierceBonus += add;
                return this;
            }

            Builder setScaldingHot(boolean v) {
                scaldingHot = v;
                return this;
            }

            Builder setAmmoReclaimChance(float chance) {
                ammoReclaimChance += chance;
                return this;
            }

            Builder setHeatPerShotMult(float mult) {
                heatPerShotMult *= mult;
                return this;
            }

            Builder addHeldMoveSpeed(float bonus) {
                heldMoveSpeedBonus += bonus;
                return this;
            }

            WeaponExtensionStats build() {
                return new WeaponExtensionStats(
                        magBonus,
                        Math.max(0.2, reloadTimeMult),
                        Math.max(0.1, damageMult),
                        Math.max(0.1, spreadMult),
                        Math.max(0.1, recoilMult),
                        Math.max(0.05, jamMult),
                        silenced,
                        Math.max(1.0, headshotBonusMult),
                        fireIntervalDelta,
                        Math.max(0.2, fireIntervalMult),
                        everyBulletCounts,
                        heavyBullets,
                        pelletDamageBonus,
                        Math.max(0, pierceBonus),
                        scaldingHot,
                        Math.min(1f, Math.max(0f, ammoReclaimChance)),
                        Math.max(0.05f, heatPerShotMult),
                        heldMoveSpeedBonus);
            }
        }
    }
}
