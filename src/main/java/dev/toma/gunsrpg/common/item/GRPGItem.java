package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import net.minecraft.item.Item;

import java.util.function.Function;

public class GRPGItem extends Item {

    public GRPGItem(String name, Item.Properties properties) {
        super(properties);
        this.setRegistryName(GunsRPG.makeResource(name));
    }

    public static GRPGItem basic(String name) {
        return basic(name, Function.identity());
    }

    public static GRPGItem basic(String name, Function<Item.Properties, Item.Properties> properties) {
        return new GRPGItem(name, properties.apply(new Item.Properties().tab(ModTabs.ITEM_TAB)));
    }
}
