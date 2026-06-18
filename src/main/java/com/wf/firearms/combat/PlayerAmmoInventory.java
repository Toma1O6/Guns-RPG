package com.wf.firearms.combat;

import com.wf.firearms.compat.backpack.SophisticatedBackpackAmmoAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 从玩家身上取弹：先按原版物品栏槽位顺序，再查精妙背包等内容物。
 * 不按材质等级优先，只按扫描顺序取第一种可用弹药。
 */
public final class PlayerAmmoInventory {
    private PlayerAmmoInventory() {}

    public static boolean hasCompatibleAmmo(Player player, FirearmSpec spec, ItemStack gun) {
        return resolveReloadAmmo(player, spec, gun) != null;
    }

    public static int countItem(Player player, Item item) {
        int total = countInVanilla(player, item);
        if (sophisticatedBackpacksLoaded()) {
            total += SophisticatedBackpackAmmoAccess.countItem(player, item);
        }
        return total;
    }

    /** 换弹时选用哪种弹药：弹匣非空时只接受与已装填相同的物品，打空后才可换材质。 */
    public static Item resolveReloadAmmo(Player player, FirearmSpec spec, ItemStack gun) {
        Item preferred = preferredAmmoItem(gun, spec);
        if (preferred != null) {
            return findFirstInOrder(player, stack -> stack.getItem() == preferred);
        }
        Set<Item> accepted = new HashSet<>(spec.acceptedAmmo());
        return findFirstInOrder(player, stack -> accepted.contains(stack.getItem()));
    }

    public static int transferAmmo(Player player, Item ammoItem, int max) {
        if (max <= 0 || ammoItem == null) {
            return 0;
        }
        int remaining = max;
        if (sophisticatedBackpacksLoaded()) {
            remaining = SophisticatedBackpackAmmoAccess.transfer(player, ammoItem, remaining);
        }
        remaining = transferFromVanilla(player, ammoItem, remaining);
        return max - remaining;
    }

    private static Item preferredAmmoItem(ItemStack gun, FirearmSpec spec) {
        if (FirearmStackState.getAmmo(gun) <= 0 || !FirearmStackState.hasLoadedAmmoType(gun)) {
            return null;
        }
        String loadedId = FirearmStackState.getLoadedAmmoItemId(gun);
        if (loadedId != null && !loadedId.isEmpty()) {
            Item item = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(
                    net.minecraft.resources.ResourceLocation.tryParse(loadedId));
            if (item != null && spec.acceptedAmmo().contains(item)) {
                return item;
            }
        }
        return null;
    }

    private static Item findFirstInOrder(Player player, Predicate<ItemStack> matcher) {
        if (sophisticatedBackpacksLoaded()) {
            Item fromBackpack = SophisticatedBackpackAmmoAccess.findFirst(player, matcher);
            if (fromBackpack != null) {
                return fromBackpack;
            }
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && matcher.test(stack)) {
                return stack.getItem();
            }
        }
        return null;
    }

    private static int countInVanilla(Player player, Item item) {
        int total = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                total += stack.getCount();
            }
        }
        return total;
    }

    private static int transferFromVanilla(Player player, Item ammoItem, int remaining) {
        for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || stack.getItem() != ammoItem) {
                continue;
            }
            int take = Math.min(remaining, stack.getCount());
            stack.shrink(take);
            remaining -= take;
        }
        return remaining;
    }

    private static boolean sophisticatedBackpacksLoaded() {
        return ModList.get().isLoaded("sophisticatedbackpacks");
    }
}
