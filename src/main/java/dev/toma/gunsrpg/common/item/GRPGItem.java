package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import net.minecraft.item.Item;

public class GRPGItem extends Item {

    public GRPGItem(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(GunsRPG.makeResource(name));
        this.setCreativeTab(ModTabs.ITEM_TAB);
    }
}
