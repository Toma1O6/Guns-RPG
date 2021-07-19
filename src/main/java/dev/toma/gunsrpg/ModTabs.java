package dev.toma.gunsrpg;

import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.common.init.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModTabs {

    public static final ItemGroup ITEM_TAB = new ItemGroup("grpg.items") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.VACCINE);
        }
    };

    public static final ItemGroup BLOCK_TAB = new ItemGroup("grpg.blocks") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.AMETHYST_ORE);
        }
    };
}
