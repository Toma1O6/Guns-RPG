package com.wf.firearms.compat.backpack;

import com.wf.firearms.GunsRpg;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * 精妙背包取弹：反射调用，避免 gunsrpg 编译期硬依赖 sophisticatedbackpacks jar。
 */
public final class SophisticatedBackpackBridge {
    private static final String PROVIDER_CLASS =
            "net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider";
    private static final String CONSUMER_CLASS =
            "net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider$BackpackInventorySlotConsumer";
    private static final String BACKPACK_ITEM_CLASS =
            "net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem";
    private static final String CAPABILITY_CLASS =
            "net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper";

    private SophisticatedBackpackBridge() {}

    public static boolean isLoaded() {
        return ModList.get().isLoaded("sophisticatedbackpacks");
    }

    public static int countItem(Player player, Item item) {
        if (!isLoaded()) {
            return 0;
        }
        int[] total = {0};
        forEachSlot(player, (stack, extract) -> {
            if (stack.getItem() == item) {
                total[0] += stack.getCount();
            }
        });
        return total[0];
    }

    public static Item findFirst(Player player, Predicate<ItemStack> matcher) {
        if (!isLoaded()) {
            return null;
        }
        Item[] found = {null};
        forEachSlot(player, (stack, extract) -> {
            if (found[0] == null && matcher.test(stack)) {
                found[0] = stack.getItem();
            }
        });
        return found[0];
    }

    public static int transfer(Player player, Item ammoItem, int max) {
        if (!isLoaded() || max <= 0) {
            return max;
        }
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
        try {
            Class<?> providerClass = Class.forName(PROVIDER_CLASS);
            Object provider = providerClass.getMethod("get").invoke(null);
            Class<?> consumerClass = Class.forName(CONSUMER_CLASS);
            Set<UUID> seen = new HashSet<>();
            Object consumer = Proxy.newProxyInstance(
                    consumerClass.getClassLoader(),
                    new Class<?>[] {consumerClass},
                    (proxy, method, args) -> {
                        if (args != null && args.length >= 1 && args[0] instanceof ItemStack backpackStack) {
                            visitBackpack(player, backpackStack, seen, visitor);
                        }
                        return true;
                    });
            Method runOnBackpacks = providerClass.getMethod("runOnBackpacks", Player.class, consumerClass);
            runOnBackpacks.invoke(provider, player, consumer);
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!stack.isEmpty()) {
                    visitBackpack(player, stack, seen, visitor);
                }
            }
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 精妙背包取弹失败", ex);
        }
    }

    private static void visitBackpack(
            Player player, ItemStack backpackStack, Set<UUID> seen, SlotVisitor visitor) {
        try {
            if (backpackStack.isEmpty()) {
                return;
            }
            Class<?> backpackItemClass = Class.forName(BACKPACK_ITEM_CLASS);
            if (!backpackItemClass.isInstance(backpackStack.getItem())) {
                return;
            }
            Object wrapper = resolveWrapper(backpackStack);
            if (wrapper == null) {
                return;
            }
            if (player.level() != null && !player.level().isClientSide) {
                Method onInit = wrapper.getClass().getMethod("onInit", net.minecraft.world.level.Level.class);
                onInit.invoke(wrapper, player.level());
            }
            Object uuidOpt = wrapper.getClass().getMethod("getContentsUuid").invoke(wrapper);
            if (uuidOpt instanceof java.util.Optional<?> opt
                    && opt.isPresent()
                    && opt.get() instanceof UUID uuid
                    && !seen.add(uuid)) {
                return;
            }
            Object handler = wrapper.getClass().getMethod("getInventoryHandler").invoke(wrapper);
            int slots = (int) handler.getClass().getMethod("getSlots").invoke(handler);
            Method getStack = handler.getClass().getMethod("getStackInSlot", int.class);
            Method extract = handler.getClass().getMethod("extractItem", int.class, int.class, boolean.class);
            for (int slot = 0; slot < slots; slot++) {
                ItemStack stack = (ItemStack) getStack.invoke(handler, slot);
                if (stack.isEmpty()) {
                    continue;
                }
                final int slotIndex = slot;
                visitor.accept(stack, count -> {
                    try {
                        extract.invoke(handler, slotIndex, count, false);
                    } catch (ReflectiveOperationException ex) {
                        GunsRpg.LOGGER.debug("[gunsrpg] 精妙背包提取物品失败", ex);
                    }
                });
            }
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 访问精妙背包失败", ex);
        }
    }

    private static Object resolveWrapper(ItemStack backpackStack) {
        try {
            Class<?> capClass = Class.forName(CAPABILITY_CLASS);
            Object capability = capClass.getField("BACKPACK_WRAPPER_CAPABILITY").get(null);
            Method getCapability =
                    ItemStack.class.getMethod("getCapability", net.minecraftforge.common.capabilities.Capability.class);
            LazyOptional<?> lazy = (LazyOptional<?>) getCapability.invoke(backpackStack, capability);
            if (lazy.isPresent()) {
                return lazy.orElse(null);
            }
            Class<?> wrapperClass =
                    Class.forName("net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper");
            return wrapperClass.getConstructor(ItemStack.class).newInstance(backpackStack);
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }
}
