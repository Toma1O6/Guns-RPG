package dev.toma.gunsrpg.util.locate.ammo;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.util.locate.AbstractLocator;
import dev.toma.gunsrpg.util.locate.IContextIterator;
import dev.toma.gunsrpg.util.locate.ILocatorPredicate;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.stream.Stream;

public final class ItemLocator extends AbstractLocator<ItemStack, IInventory> {

    private static final ItemLocator LOCATOR_INSTANCE = new ItemLocator();

    private ItemLocator() {
        super(ItemStack.EMPTY);
    }

    @Override
    public boolean isValidResult(ItemStack stack) {
        return !stack.isEmpty();
    }

    public static ILocatorPredicate<ItemStack> typeAndMaterial(IAmmoProvider provider) {
        return typeAndMaterial(provider.getAmmoType(), provider.getMaterial());
    }

    public static ILocatorPredicate<ItemStack> typeAndMaterial(AmmoType type, IAmmoMaterial material) {
        return stack -> {
            if (stack.getItem() instanceof IAmmoProvider) {
                IAmmoProvider provider = (IAmmoProvider) stack.getItem();
                return provider.getAmmoType() == type && provider.getMaterial() == material;
            }
            return false;
        };
    }

    public static ItemStack findFirst(IInventory inventory, IContextIterator<ItemStack> iterator, ILocatorPredicate<ItemStack> predicate) {
        return LOCATOR_INSTANCE.locateFirst(inventory, iterator, predicate);
    }

    public static Stream<ItemStack> findAll(IInventory inventory, IContextIterator<ItemStack> iterator, ILocatorPredicate<ItemStack> predicate) {
        return LOCATOR_INSTANCE.locateAll(inventory, iterator, predicate);
    }

    public static ItemStack findFirst(IInventory inventory, ILocatorPredicate<ItemStack> predicate) {
        return findFirst(inventory, NestedInventoryIterator.of(inventory), predicate);
    }

    public static Stream<ItemStack> findAll(IInventory inventory, ILocatorPredicate<ItemStack> predicate) {
        return findAll(inventory, NestedInventoryIterator.of(inventory), predicate);
    }

    public static int countItems(IInventory inventory, ILocatorPredicate<ItemStack> predicate) {
        return findAll(inventory, predicate).mapToInt(ItemStack::getCount).reduce(0, Integer::sum);
    }
}
