package com.wf.firearms.enchantment;

import com.wf.firearms.registry.ModGunEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

public final class GunEnchantBooks {
    private static final Map<String, RegistryObject<Enchantment>> BY_ID = new LinkedHashMap<>();

    static {
        BY_ID.put("trigger_finger", ModGunEnchantments.TRIGGER_FINGER);
        BY_ID.put("reclaimed", ModGunEnchantments.RECLAIMED);
        BY_ID.put("over_capacity", ModGunEnchantments.OVER_CAPACITY);
        BY_ID.put("lightweight", ModGunEnchantments.LIGHTWEIGHT);
        BY_ID.put("collateral", ModGunEnchantments.COLLATERAL);
        BY_ID.put("accelerator", ModGunEnchantments.ACCELERATOR);
        BY_ID.put("puncturing", ModGunEnchantments.PUNCTURING);
        BY_ID.put("quick_hands", ModGunEnchantments.QUICK_HANDS);
        BY_ID.put("fire_starter", ModGunEnchantments.FIRE_STARTER);
    }

    private GunEnchantBooks() {}

    public static ItemStack book(String shortId, int level) {
        RegistryObject<Enchantment> ench = BY_ID.get(shortId);
        if (ench == null || !ench.isPresent()) {
            return ItemStack.EMPTY;
        }
        Enchantment e = ench.get();
        int lv = Math.min(level, e.getMaxLevel());
        if (lv < 1) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(Map.of(e, lv), stack);
        return stack;
    }

    public static void addAllBooksTo(java.util.function.Consumer<ItemStack> output, int level) {
        for (String id : ModGunEnchantments.ALL_IDS) {
            ItemStack book = book(id, level);
            if (!book.isEmpty()) {
                output.accept(book);
            }
        }
    }
}
