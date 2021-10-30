package dev.toma.gunsrpg.util.helper;

import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.common.init.ModTags;
import dev.toma.gunsrpg.common.item.heal.AbstractHealItem;
import dev.toma.gunsrpg.common.item.GrenadeItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public final class StorageUtil {

    public static boolean isFood(ItemStack stack) {
        Food food = stack.getItem().getFoodProperties();
        return food != null;
    }

    public static boolean isAmmo(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof IAmmoProvider) {
            IAmmoProvider provider = (IAmmoProvider) item;
            return !provider.getAmmoType().isExplosive();
        }
        return false;
    }

    public static boolean isExplosive(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof IAmmoProvider) {
            return ((IAmmoProvider) item).getAmmoType().isExplosive();
        } return item instanceof GrenadeItem || item.is(ModTags.Items.EXPLOSIVE_ITEM);
    }

    public static boolean isMed(ItemStack stack) {
        return stack.getItem().is(ModTags.Items.HEALING_ITEM) || stack.getItem() instanceof AbstractHealItem;
    }

    /**
     * Check if provided itemstack contains inventory.
     * This method looks for NBT tag 'BlockEntityTag' which is used by vanilla inventories,
     * ItemHandler capability which is part of Forge API and should be used by mods and in case these 2
     * checks don't find all inventories, datapack makers can add specific items (by ID) to blacklist.
     *
     * @param stack ItemStack to check
     * @return {@code True} if this item doesn't contain any inventory and isn't blacklisted
     */
    public static boolean notAnInventory(ItemStack stack) {
        if (stack.getItem().is(ModTags.Items.INVENTORY_ITEM)) {
            return false; // datapack controlled blacklist
        }
        CompoundNBT nbt = stack.getTag();
        if (nbt != null) {
            return !nbt.contains("BlockEntityTag") && !nbt.contains("Items"); // vanilla inventories
        }
        LazyOptional<IItemHandler> optional = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY); // modded inventories, as long as they follow Forge API
        return !optional.isPresent();
    }

    /**
     * Private constructor because this is just utility class
     */
    private StorageUtil() {}
}
