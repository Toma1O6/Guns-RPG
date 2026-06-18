package com.wf.firearms.compat.backpack;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/** 精妙背包（Sophisticated Backpacks）取弹：佩戴背包优先，按背包内槽位顺序。 */
public final class SophisticatedBackpackAmmoAccess {
    private SophisticatedBackpackAmmoAccess() {}

    public static int countItem(Player player, Item item) {
        int[] total = {0};
        forEachSlot(player, (stack, extract) -> {
            if (stack.getItem() == item) {
                total[0] += stack.getCount();
            }
        });
        return total[0];
    }

    public static Item findFirst(Player player, Predicate<ItemStack> matcher) {
        Item[] found = {null};
        forEachSlot(player, (stack, extract) -> {
            if (found[0] == null && matcher.test(stack)) {
                found[0] = stack.getItem();
            }
        });
        return found[0];
    }

    public static int transfer(Player player, Item ammoItem, int max) {
        int[] remaining = {max};
        forEachSlot(player, (stack, extract) -> {
            if (remaining[0] <= 0 || stack.getItem() != ammoItem) {
                return;
            }
            int take = Math.min(remaining[0], stack.getCount());
            extract.accept(take);
            remaining[0] -= take;
        });
        return remaining[0];
    }

    @FunctionalInterface
    private interface SlotVisitor {
        void accept(ItemStack stack, java.util.function.IntConsumer extract);
    }

    private static void forEachSlot(Player player, SlotVisitor visitor) {
        Set<UUID> seen = new HashSet<>();
        PlayerInventoryProvider provider = PlayerInventoryProvider.get();
        provider.runOnBackpacks(player, (backpackStack, handlerName, identifier, slot) -> {
            visitBackpack(player, backpackStack, seen, visitor);
            return true;
        });
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty()) {
                visitBackpack(player, stack, seen, visitor);
            }
        }
    }

    private static void visitBackpack(
            Player player, ItemStack backpackStack, Set<UUID> seen, SlotVisitor visitor) {
        if (backpackStack.isEmpty() || !(backpackStack.getItem() instanceof BackpackItem)) {
            return;
        }
        IBackpackWrapper wrapper = resolveWrapper(backpackStack);
        if (wrapper == null) {
            return;
        }
        if (player.level() != null && !player.level().isClientSide) {
            wrapper.onInit(player.level());
        }
        var uuid = wrapper.getContentsUuid();
        if (uuid.isPresent() && !seen.add(uuid.get())) {
            return;
        }
        InventoryHandler handler = wrapper.getInventoryHandler();
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            final int slotIndex = slot;
            visitor.accept(stack, count -> handler.extractItem(slotIndex, count, false));
        }
    }

    private static IBackpackWrapper resolveWrapper(ItemStack backpackStack) {
        LazyOptional<IBackpackWrapper> capability =
                backpackStack.getCapability(CapabilityBackpackWrapper.BACKPACK_WRAPPER_CAPABILITY);
        if (capability.isPresent()) {
            return capability.orElse(null);
        }
        return new BackpackWrapper(backpackStack);
    }
}
