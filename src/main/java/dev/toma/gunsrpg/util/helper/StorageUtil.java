package dev.toma.gunsrpg.util.helper;

import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.common.item.AbstractHealItem;
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
        } return item instanceof GrenadeItem;
    }

    public static boolean isMed(ItemStack stack) {
        return stack.getItem() instanceof AbstractHealItem;
    }

    public static boolean notAnInventory(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if (nbt != null) {
            return !nbt.contains("BlockEntityTag"); // vanilla inventories
        }
        LazyOptional<IItemHandler> optional = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY); // modded inventories, as long as they follow Forge API
        return !optional.isPresent();
    }

    private StorageUtil() {}
}
