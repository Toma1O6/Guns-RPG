package dev.toma.gunsrpg.util.locate.ammo;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.util.locate.AbstractLocator;
import dev.toma.gunsrpg.util.locate.IContextIterator;
import dev.toma.gunsrpg.util.locate.ILocatorPredicate;
import dev.toma.gunsrpg.util.locate.InventoryIterator;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.stream.Stream;

public final class ItemLocator<I> extends AbstractLocator<ItemStack, I> {

    private static final ItemLocator<IInventory> LOCATOR_INSTANCE = new ItemLocator<>();

    public ItemLocator() {
        super(ItemStack.EMPTY);
    }

    public static ILocatorPredicate<ItemStack> notEmptyNorDestroyed() {
        return stack -> !stack.isEmpty() && stack.getDamageValue() < stack.getMaxDamage();
    }

    public static ILocatorPredicate<ItemStack> compatible(IAmmoProvider provider) {
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

    public static boolean hasItem(IInventory inventory, ILocatorPredicate<ItemStack> condition) {
        return !findFirst(inventory, condition).isEmpty();
    }

    public static boolean hasItem(IInventory inventory, Item item) {
        return hasItem(inventory, stack -> stack.getItem() == item);
    }

    public static ItemStack findFirst(IInventory inventory, IContextIterator<ItemStack> iterator, ILocatorPredicate<ItemStack> predicate) {
        return LOCATOR_INSTANCE.locateFirst(inventory, iterator, predicate);
    }

    public static Stream<ItemStack> findAll(IInventory inventory, IContextIterator<ItemStack> iterator, ILocatorPredicate<ItemStack> predicate) {
        return LOCATOR_INSTANCE.locateAll(inventory, iterator, predicate);
    }

    public static ItemStack findFirst(IInventory inventory, ILocatorPredicate<ItemStack> predicate) {
        return findFirst(inventory, InventoryIterator.vanilla(inventory), predicate);
    }

    public static Stream<ItemStack> findAll(IInventory inventory, ILocatorPredicate<ItemStack> predicate) {
        return findAll(inventory, InventoryIterator.vanilla(inventory), predicate);
    }

    public static int countItems(IInventory inventory, ILocatorPredicate<ItemStack> predicate) {
        return findAll(inventory, predicate).mapToInt(ItemStack::getCount).reduce(0, Integer::sum);
    }
}
