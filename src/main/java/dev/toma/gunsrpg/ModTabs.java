package dev.toma.gunsrpg;

import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.common.init.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModTabs {

    public static final ItemGroup ITEM_TAB = new ItemGroup("gunsrpg.items") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.BATTERY);
        }
    };

    public static final ItemGroup WEAPON_TAB = new ItemGroup("gunsrpg.weapons") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MK14EBR);
        }
    };

    public static final ItemGroup BLOCK_TAB = new ItemGroup("gunsrpg.blocks") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.SMITHING_TABLE);
        }
    };
}
