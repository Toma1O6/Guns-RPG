package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.init.GRPGBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.EnumHelper;

public class ItemHammer extends ItemPickaxe {

    public static final ToolMaterial WOOD_HAMMER_MATERIAL = EnumHelper.addToolMaterial("wood_hammer_material", 0, 200, 1.2F, 0.0F, 0);
    public static final ToolMaterial STONE_HAMMER_MATERIAL = EnumHelper.addToolMaterial("stone_hammer_material", 1, 450, 1.8F, 1.0F, 0);
    public static final ToolMaterial IRON_HAMMER_MATERIAL = EnumHelper.addToolMaterial("iron_hammer_material", 2, 700, 2.4F, 2.0F, 0);

    public ItemHammer(String name, ToolMaterial material) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(GunsRPG.makeResource(name));
        setCreativeTab(ModTabs.ITEM_TAB);
    }

    public BlockPos[] gatherBlocks(BlockPos pos, EnumFacing facing) {
        BlockPos[] array = new BlockPos[8];
        if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
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
        } else if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        Block block = blockIn.getBlock();
        float f = block.getBlockHardness(blockIn, null, null);
        if(f < 0) {
            return false;
        }
        if (block == Blocks.OBSIDIAN) {
            return this.toolMaterial.getHarvestLevel() == 3;
        } else if (block != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE) {
            if (block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK && block != GRPGBlocks.AMETHYST_ORE) {
                if (block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE) {
                    if (block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE) {
                        if (block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE) {
                            if (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE) {
                                Material material = blockIn.getMaterial();
                                if (material == Material.ROCK) {
                                    return true;
                                } else if (material == Material.IRON) {
                                    return true;
                                } else {
                                    return material == Material.ANVIL;
                                }
                            } else {
                                return this.toolMaterial.getHarvestLevel() >= 2;
                            }
                        } else {
                            return this.toolMaterial.getHarvestLevel() >= 1;
                        }
                    } else {
                        return this.toolMaterial.getHarvestLevel() >= 1;
                    }
                } else {
                    return this.toolMaterial.getHarvestLevel() >= 2;
                }
            } else {
                return this.toolMaterial.getHarvestLevel() >= 2;
            }
        } else {
            return this.toolMaterial.getHarvestLevel() >= 2;
        }
    }
}
