package dev.toma.gunsrpg;

import dev.toma.gunsrpg.common.init.GRPGBlocks;
import dev.toma.gunsrpg.common.init.GRPGItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModTabs {

    public static final CreativeTabs ITEM_TAB = new CreativeTabs("grpg.items") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(GRPGItems.VACCINE);
        }
    };

    public static final CreativeTabs BLOCK_TAB = new CreativeTabs("grpg.blocks") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(GRPGBlocks.AMETHYST_ORE);
        }
    };
}
