package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {

    public static void init() {
        Blocks.init();
        Items.init();
    }

    public static class Blocks {

        public static final Tags.IOptionalNamedTag<Block> ORES_AMETHYST = forge("ores/amethyst");

        private static void init() {
        }

        private static Tags.IOptionalNamedTag<Block> forge(String path) {
            return tag("forge", path);
        }

        private static Tags.IOptionalNamedTag<Block> tag(String namespace, String path) {
            return BlockTags.createOptional(new ResourceLocation(namespace, path));
        }
    }

    public static class Items {

        public static final Tags.IOptionalNamedTag<Item> ORES_AMETHYST = forge("ores/amethyst");
        public static final Tags.IOptionalNamedTag<Item> HEALING_ITEM = modded("cases/meds");
        public static final Tags.IOptionalNamedTag<Item> EXPLOSIVE_ITEM = modded("cases/explosives");
        public static final Tags.IOptionalNamedTag<Item> INVENTORY_ITEM = modded("cases/item_case_blacklist");
        public static final Tags.IOptionalNamedTag<Item> CRYSTAL = modded("crystal/crystals");
        public static final Tags.IOptionalNamedTag<Item> ORB_OF_PURITY = modded("crystal/orb_of_purity");
        public static final Tags.IOptionalNamedTag<Item> ORB_OF_TRANSMUTATION = modded("crystal/orb_of_purity");
        public static final Tags.IOptionalNamedTag<Item> PERK = modded("crystal/perk");

        private static void init() {
        }

        private static Tags.IOptionalNamedTag<Item> forge(String path) {
            return tag("forge", path);
        }

        private static Tags.IOptionalNamedTag<Item> modded(String path) {
            return tag(GunsRPG.MODID, path);
        }

        private static Tags.IOptionalNamedTag<Item> tag(String namespace, String path) {
            return ItemTags.createOptional(new ResourceLocation(namespace, path));
        }
    }
}
