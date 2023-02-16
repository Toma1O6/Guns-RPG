package dev.toma.gunsrpg.util.locate.ammo;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.locate.ILocatorPredicate;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class ItemLocator {

    public static final ItemLocator INSTANCE = new ItemLocator();

    public static ILocatorPredicate<ItemStack> filterByAmmoTypeAndMaterial(IAmmoProvider provider) {
        return filterByAmmoTypeAndMaterial(provider.getAmmoType(), provider.getMaterial());
    }

    public static ILocatorPredicate<ItemStack> filterByAmmoTypeAndMaterial(@Nullable AmmoType type, @Nullable IAmmoMaterial material) {
        return stack -> {
            Item item = stack.getItem();
            if (item instanceof IAmmoProvider) {
                IAmmoProvider provider = (IAmmoProvider) item;
                return (type == null || provider.getAmmoType() == type) && (material == null || provider.getMaterial() == material);
            }
            return false;
        };
    }

    public static ILocatorPredicate<ItemStack> notDestroyedItem() {
        return stack -> !stack.isEmpty() && stack.getDamageValue() < stack.getMaxDamage();
    }

    public static ItemStack findFirst(IInventory inventory, ILocatorPredicate<ItemStack> filter) {
        return INSTANCE.findFirst(inventory, VanillaInventoryIterator.TYPE, filter);
    }

    public static boolean consume(IInventory inventory, ILocatorPredicate<ItemStack> filter, Consumer<InventoryContext> contextConsumer) {
        return INSTANCE.consumeFirst(inventory, VanillaInventoryIterator.TYPE, filter, contextConsumer, ItemStack.EMPTY);
    }

    public static boolean contains(IInventory inventory, ILocatorPredicate<ItemStack> filter) {
        return INSTANCE.contains(inventory, VanillaInventoryIterator.TYPE, filter);
    }

    public static int sum(IInventory inventory, ILocatorPredicate<ItemStack> filter) {
        return INSTANCE.sum(inventory, VanillaInventoryIterator.TYPE, filter);
    }

    public <I, J> boolean consumeFirst(I inventory, InventoryIterator<I> iterator, ILocatorPredicate<ItemStack> filter, Consumer<InventoryContext> contextConsumer, ItemStack parent) {
        int slots = iterator.slots(inventory);
        for (int i = 0; i < slots; i++) {
            ItemStack stack = iterator.itemAt(inventory, i);
            if (stack.isEmpty()) {
                continue;
            }
            if (filter.test(stack)) {
                InventoryContext context = new InventoryContext(stack, i, inventory, parent);
                contextConsumer.accept(context);
                ModUtils.saveInventoryFromContext(context);
                return true;
            }
            InventoryHolder<J> holder = this.getInventory(stack);
            if (holder != null) {
                boolean result = consumeFirst(holder.inventory, holder.iterator, filter, contextConsumer, stack);
                if (result) {
                    return true;
                }
            }
        }
        return false;
    }

    public <I, J> ItemStack findFirst(I inventory, InventoryIterator<I> iterator, ILocatorPredicate<ItemStack> filter) {
        int slots = iterator.slots(inventory);
        for (int i = 0; i < slots; i++) {
            ItemStack stack = iterator.itemAt(inventory, i);
            if (stack.isEmpty()) {
                continue;
            }
            if (filter.test(stack)) {
                return stack;
            }
            InventoryHolder<J> holder = this.getInventory(stack);
            if (holder != null) {
                ItemStack result = findFirst(holder.inventory, holder.iterator, filter);
                if (!result.isEmpty()) {
                    return result;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public <I> boolean contains(I inventory, InventoryIterator<I> iterator, ILocatorPredicate<ItemStack> filter) {
        return !findFirst(inventory, iterator, filter).isEmpty();
    }

    public <I, J> int sum(I inventory, InventoryIterator<I> iterator, ILocatorPredicate<ItemStack> filter) {
        int sum = 0;
        int slots = iterator.slots(inventory);
        for (int i = 0; i < slots; i++) {
            ItemStack stack = iterator.itemAt(inventory, i);
            if (stack.isEmpty()) {
                continue;
            }
            if (filter.test(stack)) {
                sum += stack.getCount();
            }
            InventoryHolder<J> holder = this.getInventory(stack);
            if (holder != null) {
                int nestedSum = sum(holder.inventory, holder.iterator, filter);
                sum += nestedSum;
            }
        }
        return sum;
    }

    @SuppressWarnings("unchecked")
    private <I> InventoryHolder<I> getInventory(ItemStack stack) {
        LazyOptional<IItemHandler> optional = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (optional.isPresent()) {
            IItemHandler handler = optional.orElseThrow(IllegalStateException::new);
            if (handler instanceof IItemHandlerModifiable) {
                IItemHandlerModifiable modifiable = (IItemHandlerModifiable) handler;
                return (InventoryHolder<I>) InventoryProvider.forge(modifiable, stack);
            }
        }
        if (stack.getItem() instanceof InventoryProvider) {
            return ((InventoryProvider<I>) stack.getItem()).getInventoryHolder(stack);
        }
        return null;
    }

    public interface InventoryIterator<I> {

        int slots(I inventory);

        ItemStack itemAt(I inventory, int index);
    }

    @FunctionalInterface
    public interface InventoryProvider<I> {

        InventoryHolder<I> getInventoryHolder(ItemStack stack);

        static <I> InventoryHolder<I> of(I inventory, InventoryIterator<I> iterator, ItemStack parent) {
            return new InventoryHolder<>(inventory, iterator, parent);
        }

        static InventoryHolder<IInventory> vanilla(IInventory inventory, ItemStack parent) {
            return new InventoryHolder<>(inventory, VanillaInventoryIterator.TYPE, parent);
        }

        static InventoryHolder<IItemHandlerModifiable> forge(IItemHandlerModifiable inventory, ItemStack parent) {
            return new InventoryHolder<>(inventory, ForgeInventoryIterator.TYPE, parent);
        }
    }

    public interface SaveInventoryProvider<I> extends InventoryProvider<I> {

        void saveInventory(I inventory, ItemStack heldItem);

        void insertEditedItem(I inventory, int slot, ItemStack stack);
    }

    public static final class InventoryContext {

        private final ItemStack stack;
        private final int slotIndex;
        private final Object inventory;
        private final ItemStack parent;

        public InventoryContext(ItemStack stack, int slotIndex, Object inventory, ItemStack parent) {
            this.stack = stack;
            this.slotIndex = slotIndex;
            this.inventory = inventory;
            this.parent = parent;
        }

        public ItemStack getCurrectStack() {
            return stack;
        }

        public int getCurrentSlotIndex() {
            return slotIndex;
        }

        public Object getInventory() {
            return inventory;
        }

        public ItemStack getParent() {
            return parent;
        }
    }

    public static class InventoryHolder<I> {

        private final I inventory;
        private final InventoryIterator<I> iterator;
        private final ItemStack parent;

        private InventoryHolder(I inventory, InventoryIterator<I> iterator, ItemStack parent) {
            this.inventory = inventory;
            this.iterator = iterator;
            this.parent = parent;
        }
    }

    private static final class VanillaInventoryIterator implements InventoryIterator<IInventory> {

        private static final VanillaInventoryIterator TYPE = new VanillaInventoryIterator();

        @Override
        public int slots(IInventory inventory) {
            return inventory.getContainerSize();
        }

        @Override
        public ItemStack itemAt(IInventory inventory, int index) {
            return inventory.getItem(index);
        }
    }

    private static final class ForgeInventoryIterator implements InventoryIterator<IItemHandlerModifiable> {

        private static final ForgeInventoryIterator TYPE = new ForgeInventoryIterator();

        @Override
        public int slots(IItemHandlerModifiable inventory) {
            return inventory.getSlots();
        }

        @Override
        public ItemStack itemAt(IItemHandlerModifiable inventory, int index) {
            return inventory.getStackInSlot(index);
        }
    }
}
