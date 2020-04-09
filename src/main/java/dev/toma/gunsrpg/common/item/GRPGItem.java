package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GRPGItem extends Item {

    public static CreativeTabs ITEMS = new CreativeTabs("grpg:item") {
        @Override
        public ItemStack getTabIconItem() {
            return ItemStack.EMPTY;
        }
    };

    public GRPGItem(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(GunsRPG.makeResource(name));
    }
}
