package dev.toma.gunsrpg.util.locate.ammo;

import dev.toma.gunsrpg.util.locate.IContextIterator;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.function.BiFunction;
import java.util.function.Function;

public class NestedInventoryIterator<C> implements IContextIterator<ItemStack> {

    private final C context;
    private final BiFunction<C, Integer, ItemStack> stackFetcher;
    private final Function<C, Integer> size;

    private int currentIndex;
    private IContextIterator<ItemStack> nestedActiveIterator;

    public NestedInventoryIterator(C context, BiFunction<C, Integer, ItemStack> stackFetcher, Function<C, Integer> size) {
        this.context = context;
        this.stackFetcher = stackFetcher;
        this.size = size;
    }

    public static NestedInventoryIterator<IInventory> of(IInventory inventory) {
        return new NestedInventoryIterator<>(inventory, IInventory::getItem, IInventory::getContainerSize);
    }

    public static NestedInventoryIterator<IItemHandler> of(IItemHandler inventory) {
        return new NestedInventoryIterator<>(inventory, IItemHandler::getStackInSlot, IItemHandler::getSlots);
    }

    @Override
    public ItemStack next() {
        if (nestedActiveIterator != null) {
            ItemStack stack = walkThroughNested();
            if (!stack.isEmpty())
                return stack;
        }
        ItemStack result = stackFetcher.apply(context, currentIndex++);
        LazyOptional<IItemHandler> optional = result.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (optional.isPresent()) {
            IItemHandler handler = optional.orElseThrow(IllegalStateException::new);
            nestedActiveIterator = of(handler);
            ItemStack first = walkThroughNested();
            if (!first.isEmpty())
                return first;
        }
        return result;
    }

    @Override
    public boolean hasNext() {
        return size.apply(context) > currentIndex;
    }

    private ItemStack walkThroughNested() {
        ItemStack stack = nestedActiveIterator.next();
        if (!stack.isEmpty()) {
            return stack;
        }
        nestedActiveIterator = null;
        return ItemStack.EMPTY;
    }
}
