package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class HammerItem extends PickaxeItem {

    public static final IItemTier WOOD_HAMMER_MATERIAL = new TierImpl(200, 1.2F, 0.0F, 0, 0, () -> Ingredient.of(ItemTags.PLANKS));
    public static final IItemTier STONE_HAMMER_MATERIAL = new TierImpl(450, 1.8F, 1.0F, 1, 0, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS));
    public static final IItemTier IRON_HAMMER_MATERIAL = new TierImpl(700, 2.4F, 2.0F, 2, 0, () -> Ingredient.of(Items.IRON_INGOT));

    public HammerItem(String name, IItemTier material) {
        super(material, 1, -2.8F, new Properties().tab(ModTabs.ITEM_TAB).stacksTo(1));
        setRegistryName(GunsRPG.makeResource(name));
    }

    public BlockPos[] gatherBlocks(BlockPos pos, Direction direction) {
        BlockPos[] array = new BlockPos[8];
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            int ox = pos.getX() - 1;
            int oy = pos.getY() - 1;
            int i = 0;
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (x == 1 && y == 1) continue;
                    array[i] = new BlockPos(ox + x, oy + y, pos.getZ());
                    i++;
                }
            }
        } else if (direction == Direction.EAST || direction == Direction.WEST) {
            int oz = pos.getZ() - 1;
            int oy = pos.getY() - 1;
            int i = 0;
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    if (z == 1 && y == 1) continue;
                    array[i] = new BlockPos(pos.getX(), oy + y, oz + z);
                    i++;
                }
            }
        } else {
            int ox = pos.getX() - 1;
            int oz = pos.getZ() - 1;
            int i = 0;
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    if (x == 1 && z == 1) continue;
                    array[i] = new BlockPos(ox + x, pos.getY(), oz + z);
                    i++;
                }
            }
        }
        return array;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }
}
