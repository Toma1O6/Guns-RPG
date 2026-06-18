package com.wf.firearms.combat;



import com.wf.firearms.data.PlayerFirearmsData;

import com.wf.firearms.data.WeaponMapping;

import com.wf.firearms.registry.ModItems;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;



import java.util.Optional;



/**

 * 维修站修理：

 * <ul>

 *   <li>仍有剩余耐久（如 990/1000）：上限减少 = 已损耗 × (1 − 保留率)，例：10×0.3=3 → 997/997</li>

 *   <li>完全损坏（0/1000）：上限 × 保留率（默认 0.7）→ 700/700</li>

 * </ul>

 */

public final class WeaponRepairService {

    public static final float BASE_RETENTION = 0.7f;



    private WeaponRepairService() {}



    public static Optional<String> weaponKey(ItemStack stack) {

        return WeaponMapping.weaponKeyFromStack(stack);

    }



    public static boolean isRepairableWeapon(ItemStack stack) {

        return weaponKey(stack).isPresent();

    }



    public static boolean isRepairKit(ItemStack stack) {

        return !stack.isEmpty() && stack.is(ModItems.WEAPON_REPAIR_KIT.get());

    }



    public static float retentionMultiplier(Player player) {

        float m = BASE_RETENTION;

        if (PlayerFirearmsData.isUnlocked(player, "repair_man_iii")) {

            m += 0.20f;

        } else if (PlayerFirearmsData.isUnlocked(player, "repair_man_ii")) {

            m += 0.10f;

        } else if (PlayerFirearmsData.isUnlocked(player, "repair_man_i")) {

            m += 0.05f;

        }

        return Math.min(1.0f, m);

    }



    /** 修理后的有效最大磨损点数。 */

    public static int projectedMaxWearAfterRepair(Player player, ItemStack gun, String weaponKey) {

        return computeNewMaxWear(player, gun, weaponKey);

    }



    /** 修理后相对出厂上限的比例（用于 UI 条）。 */

    public static float projectedLimitRatioAfterRepair(Player player, ItemStack gun, String weaponKey) {

        int baseMax = WeaponWearRegistry.maxWear(weaponKey);

        if (baseMax <= 0) {

            return 1.0f;

        }

        return projectedMaxWearAfterRepair(player, gun, weaponKey) / (float) baseMax;

    }



    public static boolean canRepair(Player player, ItemStack gun, String weaponKey, ItemStack repairKit) {

        return WeaponWearState.canRepair(gun, weaponKey)

                && isRepairKit(repairKit)

                && repairKit.getDamageValue() < repairKit.getMaxDamage();

    }



    public static void performRepair(Player player, ItemStack gun, String weaponKey, ItemStack repairKit) {

        int newMax = computeNewMaxWear(player, gun, weaponKey);

        int baseMax = WeaponWearRegistry.maxWear(weaponKey);

        float newLimit = baseMax > 0 ? (float) newMax / baseMax : 1.0f;

        WeaponWearState.repair(gun, newLimit);

        if (!player.getAbilities().instabuild) {

            damageRepairKit(repairKit);

        }

    }



    /** 对齐原版：逐点消耗维修包耐久，用尽则消耗 1 个。 */

    public static void damageRepairKit(ItemStack kit) {

        if (kit.isEmpty() || !isRepairKit(kit)) {

            return;

        }

        int dmg = kit.getDamageValue();

        if (dmg + 1 >= kit.getMaxDamage()) {

            kit.shrink(1);

            if (!kit.isEmpty()) {

                kit.setDamageValue(0);

            }

        } else {

            kit.setDamageValue(dmg + 1);

        }

    }



    private static int computeNewMaxWear(Player player, ItemStack gun, String weaponKey) {

        int wearBefore = WeaponWearState.getWear(gun);

        int currentMax = WeaponWearState.getEffectiveMaxWear(gun, weaponKey);

        int remaining = WeaponWearState.remainingWear(gun, weaponKey);

        float retention = retentionMultiplier(player);

        float penaltyRate = 1.0f - retention;



        if (remaining <= 0 || wearBefore >= currentMax) {

            return Math.max(1, Math.round(currentMax * retention));

        }

        return Math.max(1, currentMax - Math.round(wearBefore * penaltyRate));

    }

}


