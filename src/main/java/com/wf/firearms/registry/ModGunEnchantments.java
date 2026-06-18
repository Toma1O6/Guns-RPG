package com.wf.firearms.registry;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.enchantment.GunEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

/** 原 CGM 枪械附魔，迁为 gunsrpg 自有（可卸 CGM）。 */
public final class ModGunEnchantments {
    public static final Set<String> ALL_IDS =
            Set.of(
                    "trigger_finger",
                    "reclaimed",
                    "over_capacity",
                    "lightweight",
                    "collateral",
                    "accelerator",
                    "puncturing",
                    "quick_hands",
                    "fire_starter");

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, GunsRpg.MOD_ID);

    public static final RegistryObject<Enchantment> TRIGGER_FINGER =
            reg("trigger_finger", Enchantment.Rarity.UNCOMMON, 4);
    public static final RegistryObject<Enchantment> RECLAIMED =
            reg("reclaimed", Enchantment.Rarity.RARE, 3);
    public static final RegistryObject<Enchantment> OVER_CAPACITY =
            reg("over_capacity", Enchantment.Rarity.UNCOMMON, 4);
    public static final RegistryObject<Enchantment> LIGHTWEIGHT =
            reg("lightweight", Enchantment.Rarity.UNCOMMON, 3);
    public static final RegistryObject<Enchantment> COLLATERAL =
            reg("collateral", Enchantment.Rarity.RARE, 3);
    public static final RegistryObject<Enchantment> ACCELERATOR =
            reg("accelerator", Enchantment.Rarity.UNCOMMON, 4);
    public static final RegistryObject<Enchantment> PUNCTURING =
            reg("puncturing", Enchantment.Rarity.RARE, 3);
    public static final RegistryObject<Enchantment> QUICK_HANDS =
            reg("quick_hands", Enchantment.Rarity.UNCOMMON, 4);
    public static final RegistryObject<Enchantment> FIRE_STARTER =
            reg("fire_starter", Enchantment.Rarity.UNCOMMON, 2);

    private ModGunEnchantments() {}

    public static boolean isGunEnchant(String path) {
        return path != null && ALL_IDS.contains(path);
    }

    private static RegistryObject<Enchantment> reg(String id, Enchantment.Rarity rarity, int max) {
        return ENCHANTMENTS.register(id, () -> new GunEnchantment(rarity, max));
    }
}
