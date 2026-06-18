package com.wf.firearms.gameplay;



import com.wf.firearms.combat.FirearmRegistry;

import com.wf.firearms.combat.FirearmSpec;

import com.wf.firearms.data.PlayerFirearmsData;

import net.minecraft.nbt.CompoundTag;

import net.minecraft.world.effect.MobEffectInstance;

import net.minecraft.world.effect.MobEffects;

import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.player.Player;



/**

 * 武器扩展天赋中需运行时状态或条件判定的效果（距离增伤、叠层、背身三枪等）。

 */

public final class WeaponExtensionCombat {

    private static final String ROOT = "wf_ext_combat";

    private static final String LAST_SHOT_TICK = "last_shot_tick";

    private static final String HOT_STACKS = "hot_stacks";

    private static final String HOT_DAMAGE_STACKS = "hot_damage_stacks";

    private static final String BACK_CHARGES = "back_charges";

    private static final String BACK_ACTIVE_CHARGES = "back_active_charges";

    private static final String VETERAN_READY = "veteran_ready";

    private static final String VETERAN_ACTIVE = "veteran_active";

    private static final String VETERAN_NEXT = "veteran_next";



    private static final int NEVER_GIVE_UP_DURATION = 100;

    private static final int BACK_IDLE_TICKS = 100;

    private static final int BACK_MAX_CHARGES = 3;

    private static final int VETERAN_INTERVAL_TICKS = 100;

    private static final int GLORY_KILL_ABSORPTION_DURATION = 600;

    private static final double BRUTAL_CANNON_RANGE = 8.0;

    private static final double ATROCITY_NEAR_RANGE = 4.0;

    private static final double ATROCITY_FAR_RANGE = 8.0;

    private static final double CLOSE_QUARTERS_RANGE = 8.0;

    private static final int ARMOR_BANE_THRESHOLD = 10;

    private static final float HOT_HANDS_BONUS_PER_STACK = 0.10f;

    private static final int HOT_INTERVAL_GRACE_TICKS = 2;



    private WeaponExtensionCombat() {}



    public static float heldMoveSpeedBonus(Player player, String weaponKey) {
        float bonus = WeaponExtensionService.stats(player, weaponKey).heldMoveSpeedBonus();
        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "quick_shift")) {
            bonus += 0.20f;
        }
        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "quick_mover")) {
            bonus += 0.30f;
        }
        return bonus;
    }



    public static int pelletCount(Player player, String weaponKey, int basePellets) {

        if (basePellets <= 0) {

            return basePellets;

        }

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "explosive_shot")) {
            return Math.max(basePellets, 3);
        }

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "cannon_blast")) {

            return basePellets * 2;

        }

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "shrapnel")) {

            return basePellets + 2;

        }

        return basePellets;

    }



    public static double wearChanceMultiplier(Player player, String weaponKey) {

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "precision_care")) {

            return 0.85;

        }

        return 1.0;

    }



    public static double extraJamChanceMultiplier(Player player, String weaponKey) {

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "precision_care")) {

            return 0.75;

        }

        return 1.0;

    }



    public static float recoilMultiplier(Player player, String weaponKey) {

        if (backThreeCharges(player, weaponKey) > 0) {

            return 0f;

        }

        return 1f;

    }

    /** 枪械命中后附加效果（击退等）。 */
    public static void onGunHit(Player player, String weaponKey, LivingEntity target) {
        if (player == null || target == null || weaponKey == null || weaponKey.isEmpty()) {
            return;
        }
        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "power_knockback")) {
            double dx = player.getX() - target.getX();
            double dz = player.getZ() - target.getZ();
            target.knockback(0.55f, dx, dz);
        }
        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "blazing_pellets")) {
            target.setSecondsOnFire(4);
        }
    }



    public static void onGunFire(Player player, String weaponKey) {

        if (player == null || weaponKey == null || weaponKey.isEmpty()) {

            return;

        }

        long now = player.level().getGameTime();

        CompoundTag tag = combatTag(player, weaponKey);



        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "back_three")) {

            long last = tag.getLong(LAST_SHOT_TICK);

            int charges = tag.getInt(BACK_CHARGES);

            if (last > 0 && now - last >= BACK_IDLE_TICKS) {

                charges = BACK_MAX_CHARGES;

            }

            tag.putInt(BACK_ACTIVE_CHARGES, charges);

            if (charges > 0) {

                tag.putInt(BACK_CHARGES, charges - 1);

            } else {

                tag.putInt(BACK_CHARGES, 0);

            }

        } else {

            tag.remove(BACK_ACTIVE_CHARGES);

        }



        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "hot_hands")) {

            FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);

            int interval = effectiveFireIntervalTicks(player, weaponKey, spec);

            long last = tag.getLong(LAST_SHOT_TICK);

            int cap = hotHandsStackCap(player, weaponKey, spec);

            int prevBurst = tag.getInt(HOT_STACKS);

            boolean within = last > 0 && now - last <= interval + HOT_INTERVAL_GRACE_TICKS;

            int shotsInBurst = within ? prevBurst + 1 : 1;

            int stacksForDamage = shotsInBurst >= cap ? cap : Math.max(0, shotsInBurst - 1);

            tag.putInt(HOT_DAMAGE_STACKS, stacksForDamage);

            tag.putInt(HOT_STACKS, shotsInBurst);

        } else {

            tag.remove(HOT_DAMAGE_STACKS);

            tag.remove(HOT_STACKS);

        }

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "veteran_hunter")) {
            long nextReady = tag.getLong(VETERAN_NEXT);
            if (!tag.getBoolean(VETERAN_READY) && now >= nextReady) {
                tag.putBoolean(VETERAN_READY, true);
            }
            if (tag.getBoolean(VETERAN_READY)) {
                tag.putBoolean(VETERAN_ACTIVE, true);
                tag.putBoolean(VETERAN_READY, false);
                tag.putLong(VETERAN_NEXT, now + VETERAN_INTERVAL_TICKS);
            } else if (!tag.contains(VETERAN_NEXT)) {
                tag.putLong(VETERAN_NEXT, now + VETERAN_INTERVAL_TICKS);
            }
        } else {
            tag.remove(VETERAN_READY);
            tag.remove(VETERAN_ACTIVE);
            tag.remove(VETERAN_NEXT);
        }



        tag.putLong(LAST_SHOT_TICK, now);

    }



    public static void onGunKill(Player player, String weaponKey) {

        if (player == null || weaponKey == null) {

            return;

        }

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "never_give_up")) {

            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, NEVER_GIVE_UP_DURATION, 0, true, true, true));

        }

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "glory_kill")) {
            player.addEffect(
                    new MobEffectInstance(MobEffects.ABSORPTION, GLORY_KILL_ABSORPTION_DURATION, 0, true, true, true));
            player.heal(2.0f);
        }

    }



    public static float damageMultiplier(

            Player player, String weaponKey, LivingEntity target, float baseDamage) {

        if (player == null || weaponKey == null || target == null) {

            return baseDamage;

        }

        float mult = 1.0f;



        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "brutal_cannon")) {

            double dist = Math.sqrt(player.distanceToSqr(target));

            if (dist <= BRUTAL_CANNON_RANGE) {

                mult *= 1.5f;

            }

        }

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "atrocity")) {
            double dist = Math.sqrt(player.distanceToSqr(target));
            if (dist <= ATROCITY_NEAR_RANGE) {
                mult *= 3.0f;
            } else if (dist <= ATROCITY_FAR_RANGE) {
                mult *= 2.0f;
            }
        }

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "close_quarters")) {
            double dist = Math.sqrt(player.distanceToSqr(target));
            if (dist <= CLOSE_QUARTERS_RANGE) {
                mult *= 1.6f;
            }
        }

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "armor_bane")
                && target.getArmorValue() >= ARMOR_BANE_THRESHOLD) {
            mult *= 1.3f;
        }

        CompoundTag tag = combatTag(player, weaponKey);
        if (tag.getBoolean(VETERAN_ACTIVE)) {
            mult *= 4.0f;
            tag.putBoolean(VETERAN_ACTIVE, false);
        }



        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "finisher")

                && target.getHealth() <= target.getMaxHealth() * 0.5f) {

            mult *= 1.25f;

        }

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "cruel")

                && target.getHealth() <= target.getMaxHealth() * 0.5f) {

            mult *= 1.3f;

        }



        if (backThreeCharges(player, weaponKey) > 0) {

            mult *= 1.2f;

        }



        int hotStacks = hotHandsDamageStacks(player, weaponKey);

        if (hotStacks > 0) {

            mult *= 1.0f + hotStacks * HOT_HANDS_BONUS_PER_STACK;

        }

        if (WeaponExtensionIds.isUnlocked(player, weaponKey, "barrel_overheat")) {
            net.minecraft.world.item.ItemStack gun = player.getMainHandItem();
            if (com.wf.firearms.compat.tacz.TaczBridge.heatRatio(gun) > 0.5f) {
                mult *= 1.2f;
            }
        }

        return baseDamage * mult;

    }



    public static int hotHandsDamageStacks(Player player, String weaponKey) {

        if (!WeaponExtensionIds.isUnlocked(player, weaponKey, "hot_hands")) {

            return 0;

        }

        return combatTag(player, weaponKey).getInt(HOT_DAMAGE_STACKS);

    }



    public static int backThreeCharges(Player player, String weaponKey) {

        if (!WeaponExtensionIds.isUnlocked(player, weaponKey, "back_three")) {

            return 0;

        }

        return combatTag(player, weaponKey).getInt(BACK_ACTIVE_CHARGES);

    }



    private static int hotHandsStackCap(Player player, String weaponKey, FirearmSpec spec) {

        int mag = WeaponExtensionService.magazineCapacity(player, weaponKey, spec.magazineSize());

        return Math.max(1, mag);

    }



    private static int effectiveFireIntervalTicks(Player player, String weaponKey, FirearmSpec spec) {

        WeaponExtensionService.WeaponExtensionStats ext = WeaponExtensionService.stats(player, weaponKey);

        return Math.max(

                1,

                (int)

                        Math.round(

                                spec.fireIntervalTicks() * ext.fireIntervalMult()

                                        + ext.fireIntervalDelta()));

    }



    private static CompoundTag combatTag(Player player, String weaponKey) {

        CompoundTag root = PlayerFirearmsData.root(player);

        CompoundTag all = root.contains(ROOT) ? root.getCompound(ROOT) : new CompoundTag();

        if (!root.contains(ROOT)) {

            root.put(ROOT, all);

        }

        CompoundTag gun = all.contains(weaponKey) ? all.getCompound(weaponKey) : new CompoundTag();

        if (!all.contains(weaponKey)) {

            all.put(weaponKey, gun);

        }

        return gun;

    }

}


