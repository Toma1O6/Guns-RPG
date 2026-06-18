package com.wf.firearms.compat.tacz;

import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/** TaCZ 枪物品 NBT 标记 wf 武器 key（与 CGM 共用 tag 名，便于迁移存档）。 */
public final class TaczWeaponBinding {
    public static final String TAG = "gunsrpg_weapon_key";

    private TaczWeaponBinding() {}

    public static void bind(ItemStack stack, String weaponKey) {
        if (stack.isEmpty() || weaponKey == null || weaponKey.isEmpty()) {
            return;
        }
        stack.getOrCreateTag().putString(TAG, weaponKey);
    }

    public static Optional<String> read(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTag() || !stack.getTag().contains(TAG)) {
            return Optional.empty();
        }
        String key = stack.getTag().getString(TAG);
        return key.isEmpty() ? Optional.empty() : Optional.of(key);
    }
}
