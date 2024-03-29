package dev.toma.gunsrpg.common.item.perk;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.init.ModTags;
import dev.toma.gunsrpg.common.item.BaseItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;

public class PerkItem extends BaseItem {

    protected final PerkVariant variant;

    protected PerkItem(String name, PerkVariant variant, Properties properties) {
        super(name, properties);
        this.variant = variant;
    }

    public PerkItem(String name, PerkVariant variant) {
        this(name, variant, new Properties().tab(ModTabs.ITEM_TAB));
    }

    public PerkVariant getVariant() {
        return variant;
    }

    public static boolean isOrbOfPurity(ItemStack stack) {
        return isPerkAndTagged(stack, ModTags.Items.ORB_OF_PURITY);
    }

    public static boolean isOrbOfTransmutation(ItemStack stack) {
        return isPerkAndTagged(stack, ModTags.Items.ORB_OF_TRANSMUTATION);
    }

    private static boolean isPerkAndTagged(ItemStack stack, ITag<Item> tag) {
        Item item = stack.getItem();
        return item.is(tag) && item instanceof PerkItem;
    }
}
