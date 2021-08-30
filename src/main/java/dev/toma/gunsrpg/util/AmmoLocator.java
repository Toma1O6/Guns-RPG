package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class AmmoLocator {

    public boolean hasAmmo(IInventory inventory, ISearchConstraint constraint) {
        return !findFirst(inventory, constraint).isEmpty();
    }

    public int count(IInventory inventory, ISearchConstraint constraint) {
        return count(inventory, IInventory::getContainerSize, IInventory::getItem, constraint);
    }

    public int count(IItemHandler handler, ISearchConstraint constraint) {
        return count(handler, IItemHandler::getSlots, IItemHandler::getStackInSlot, constraint);
    }

    public <I> int count(I inventoryType, Function<I, Integer> sizeFunc, BiFunction<I, Integer, ItemStack> stackFetcher, ISearchConstraint constraint) {
        int total = 0;
        int len = sizeFunc.apply(inventoryType);
        for (int i = 0; i < len; i++) {
            ItemStack stack = stackFetcher.apply(inventoryType, i);
            if (stack.getItem() instanceof IAmmoProvider) {
                IAmmoProvider provider = (IAmmoProvider) stack.getItem();
                if (constraint.isValidItem(provider)) {
                    total += stack.getCount();
                }
            } else {
                LazyOptional<IItemHandler> optional = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                if (optional.isPresent()) {
                    IItemHandler itemHandler = optional.orElseThrow(IllegalStateException::new);
                    total += count(itemHandler, constraint);
                }
            }
        }
        return total;
    }

    public ItemStack findFirst(IInventory inventory, ISearchConstraint constraint) {
        return findFirst(inventory, IInventory::getItem, constraint);
    }

    public <I> ItemStack findFirst(I inventory, BiFunction<I, Integer, ItemStack> fetcher, ISearchConstraint constraint) {
        return findNext(0, inventory, fetcher, constraint);
    }

    public <I> ItemStack findNext(int index, I inventory, BiFunction<I, Integer, ItemStack> fetcher, ISearchConstraint constraint) {
        ItemStack stack = fetcher.apply(inventory, index);
        if (stack.getItem() instanceof IAmmoProvider) {
            IAmmoProvider provider = (IAmmoProvider) stack.getItem();
            if (constraint.isValidItem(provider)) {
                return stack;
            }
        } else {
            LazyOptional<IItemHandler> optional = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            if (optional.isPresent()) {
                IItemHandler handler = optional.orElseThrow(IllegalStateException::new);
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack nested = findNext(i, handler, IItemHandler::getStackInSlot, constraint);
                    if (!nested.isEmpty()) {
                        return nested;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @FunctionalInterface
    public interface ISearchConstraint {

        ISearchConstraint ALL = provider -> true;

        boolean isValidItem(IAmmoProvider provider);

        static ISearchConstraint typeAndMaterial(AmmoType type, IAmmoMaterial material) {
            return provider -> provider.getAmmoType() == type && provider.getMaterial() == material;
        }
    }
}
