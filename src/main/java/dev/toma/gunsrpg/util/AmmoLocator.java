package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class AmmoLocator {

    public int count(PlayerInventory inventory, ISearchConstraint constraint) {
        return count(inventory, PlayerInventory::getContainerSize, PlayerInventory::getItem, constraint);
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

    @FunctionalInterface
    public interface ISearchConstraint {

        ISearchConstraint ALL = provider -> true;

        boolean isValidItem(IAmmoProvider provider);

        static ISearchConstraint typeAndMaterial(AmmoType type, AmmoMaterial material) {
            return provider -> provider.getAmmoType() == type && provider.getMaterial() == material;
        }
    }
}
