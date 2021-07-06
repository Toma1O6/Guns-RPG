package dev.toma.gunsrpg;

import dev.toma.gunsrpg.common.init.GRPGBlocks;
import dev.toma.gunsrpg.common.init.GRPGItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModTabs {

    public static final ItemGroup ITEM_TAB = new ItemGroup("grpg.items") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(GRPGItems.VACCINE);
        }
    };

    public static final ItemGroup BLOCK_TAB = new ItemGroup("grpg.blocks") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(GRPGBlocks.AMETHYST_ORE);
        }
    };
}
