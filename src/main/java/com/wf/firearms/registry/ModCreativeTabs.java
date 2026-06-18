package com.wf.firearms.registry;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.enchantment.GunEnchantBooks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GunsRpg.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register(
            "main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gunsrpg.main"))
                    .icon(() -> new ItemStack(ModItems.GUN_PARTS.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModBlocks.GUNSMITH_TABLE_ITEM.get());
                        output.accept(ModBlocks.CULINARY_TABLE_ITEM.get());
                        output.accept(ModBlocks.REPAIR_STATION_ITEM.get());
                        output.accept(ModBlocks.MEDICAL_STATION_ITEM.get());
                        output.accept(ModItems.GUN_PARTS.get());
                        output.accept(ModItems.COOKING_OIL.get());
                        output.accept(ModItems.BARREL.get());
                        output.accept(ModItems.MAGAZINE.get());
                        output.accept(ModItems.WOODEN_STOCK.get());
                        output.accept(ModItems.LONG_BARREL.get());
                        output.accept(ModItems.SMALL_IRON_STOCK.get());
                        output.accept(ModItems.SMALL_BULLET_CASING.get());
                        output.accept(ModItems.LARGE_BULLET_CASING.get());
                        output.accept(ModItems.SHOTGUN_SHELL.get());
                        output.accept(ModItems.BOLT_FLETCHING.get());
                        output.accept(ModItems.SKILLPOINT_BOOK.get());
                        output.accept(ModItems.PERKPOINT_BOOK.get());
                        output.accept(ModItems.WEAPON_BOOK.get());
                        for (Item ammo : ModItems.allAmmoItems()) {
                            output.accept(ammo);
                        }
                        output.accept(ModItems.WEAPON_REPAIR_KIT.get());
                        for (Item grenade : ModItems.allGrenades()) {
                            output.accept(grenade);
                        }
                        output.accept(ModItems.BLOODMOON_GOLEM_SPAWN_EGG.get());
                        output.accept(ModItems.ROCKET_ANGEL_SPAWN_EGG.get());
                        output.accept(ModItems.ZOMBIE_GUNNER_SPAWN_EGG.get());
                        output.accept(ModItems.EXPLOSIVE_SKELETON_SPAWN_EGG.get());
                        for (String key : CreativeGunCatalog.gunKeys()) {
                            ItemStack stack = CreativeGunCatalog.creativeStack(key);
                            if (!stack.isEmpty()) {
                                output.accept(stack);
                            }
                        }
                        for (Item shell : ModItems.allLauncherShells()) {
                            output.accept(shell);
                        }
                        if (TaczBackendConfig.useTaczShooting()) {
                            GunEnchantBooks.addAllBooksTo(output::accept, 1);
                        }
                    })
                    .build());

    private ModCreativeTabs() {}
}
