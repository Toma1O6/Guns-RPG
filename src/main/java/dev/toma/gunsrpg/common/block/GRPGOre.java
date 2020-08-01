package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.ModRegistry;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;
import java.util.function.Supplier;

public class GRPGOre extends BlockOre {

    private final Supplier<Item> supplier;
    private final int min, max;
    private final int addedFortune;

    public GRPGOre(String name) {
        this(name, null);
    }

    public GRPGOre(String name, Supplier<Item> dropItem) {
        this(name, dropItem, 1, 1, 1);
    }

    public GRPGOre(String name, Supplier<Item> dropItem, int min, int max, int addedFortune) {
        super(Material.ROCK.getMaterialMapColor());
        setUnlocalizedName(name);
        setRegistryName(name);
        this.setCreativeTab(ModTabs.BLOCK_TAB);
        this.supplier = dropItem;
        this.min = min;
        this.max = max;
        this.addedFortune = addedFortune;
        this.setHardness(2.5F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 2);
        ModRegistry.registerItemBlock(this);
    }

    @Override
    public Material getMaterial(IBlockState state) {
        return Material.IRON;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return supplier != null ? supplier.get() : super.getItemDropped(state, rand, fortune);
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return min + random.nextInt(max + addedFortune * fortune);
    }
}
