package com.wf.firearms.combat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/** 枪械射击损耗（NBT 独立于 CGM 弹量条）。 */
public final class WeaponWearState {
    private static final String WEAR = "gunsrpg_wear";
    private static final String WEAR_LIMIT = "gunsrpg_wear_limit";

    private WeaponWearState() {}

    public static int getWear(ItemStack gun) {
        CompoundTag tag = gun.getTag();
        return tag != null ? Math.max(0, tag.getInt(WEAR)) : 0;
    }

    public static void setWear(ItemStack gun, int wear) {
        gun.getOrCreateTag().putInt(WEAR, Math.max(0, wear));
    }

    public static void addWear(ItemStack gun, String weaponKey, int amount) {
        if (amount <= 0) {
            return;
        }
        setWear(gun, Math.min(getEffectiveMaxWear(gun, weaponKey), getWear(gun) + amount));
    }

    public static void setWearLimit(ItemStack gun, float limit) {
        gun.getOrCreateTag().putFloat(WEAR_LIMIT, Math.max(0.05f, Math.min(1.0f, limit)));
    }

    /** 维修站：清零损耗并按比例降低上限。 */
    public static void repair(ItemStack gun, float newLimitRatio) {
        setWear(gun, 0);
        setWearLimit(gun, newLimitRatio);
    }

    public static boolean canRepair(ItemStack gun, String weaponKey) {
        return !gun.isEmpty()
                && weaponKey != null
                && getWear(gun) > 0
                && getWearLimit(gun) > 0.11f
                && !isDestroyed(gun, weaponKey);
    }

    /** 维修后上限比例（1.0 = 满上限；维修站可写入 &lt; 1）。 */
    public static float getWearLimit(ItemStack gun) {
        CompoundTag tag = gun.getTag();
        if (tag == null || !tag.contains(WEAR_LIMIT)) {
            return 1.0f;
        }
        return Math.max(0.05f, Math.min(1.0f, tag.getFloat(WEAR_LIMIT)));
    }

    public static int getEffectiveMaxWear(ItemStack gun, String weaponKey) {
        return Math.max(1, Math.round(WeaponWearRegistry.maxWear(weaponKey) * getWearLimit(gun)));
    }

    public static int remainingWear(ItemStack gun, String weaponKey) {
        return Math.max(0, getEffectiveMaxWear(gun, weaponKey) - getWear(gun));
    }

    /** 剩余耐久比例 0~1（用于 UI 条）。 */
    public static float conditionRatio(ItemStack gun, String weaponKey) {
        int max = getEffectiveMaxWear(gun, weaponKey);
        if (max <= 0) {
            return 0f;
        }
        return Math.max(0f, Math.min(1f, remainingWear(gun, weaponKey) / (float) max));
    }

    public static boolean isDestroyed(ItemStack gun, String weaponKey) {
        return getWear(gun) >= getEffectiveMaxWear(gun, weaponKey);
    }

    public static boolean shouldShowWearBar(ItemStack gun, String weaponKey) {
        return getWear(gun) > 0 || getWearLimit(gun) < 0.999f;
    }

    /** 对齐原版 Guns RPG 耐久条配色（damageFraction = 已损耗 / 上限）。 */
    public static int barColor(ItemStack gun, String weaponKey) {
        float damageFraction = 1.0f - conditionRatio(gun, weaponKey);
        if (damageFraction < 0.4f) {
            float f = damageFraction / 0.4f;
            int blue = (int) ((1.0f - f) * 255);
            return 0xFF00FF00 | (blue << 8);
        }
        float t = (damageFraction - 0.4f) / 0.6f;
        t = t * t * t;
        int red = (int) (255 * t);
        int green = (int) (255 * (1.0f - t));
        return 0xFF000000 | (red << 16) | (green << 8);
    }
}
