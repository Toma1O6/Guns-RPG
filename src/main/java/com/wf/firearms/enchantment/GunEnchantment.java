package com.wf.firearms.enchantment;

import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.compat.tacz.TaczBridge;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/** 仅可附在 TaCZ 枪械上（不依赖 CGM）。 */
public final class GunEnchantment extends Enchantment {
    private final int maxLevel;

    public GunEnchantment(Rarity rarity, int maxLevel) {
        super(rarity, EnchantmentCategory.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
        this.maxLevel = Math.max(1, maxLevel);
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return TaczCompat.isTaczLoaded() && TaczBridge.isTaczGun(stack);
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }

    @Override
    public boolean isTradeable() {
        return true;
    }
}
