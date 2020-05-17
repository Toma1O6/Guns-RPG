package dev.toma.gunsrpg.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;
import java.util.function.Supplier;

public class GRPGOre extends GRPGBlock {

    private Supplier<Item> supplier;
    private int min, max;
    private int addedFortune;

    public GRPGOre(String name) {
        this(name, null);
    }

    public GRPGOre(String name, Supplier<Item> dropItem) {
        this(name, dropItem, 1, 1, 1);
    }

    public GRPGOre(String name, Supplier<Item> dropItem, int min, int max, int addedFortune) {
        super(name, Material.ROCK);
        this.supplier = dropItem;
        this.min = min;
        this.max = max;
        this.addedFortune = addedFortune;
        this.setHardness(2.5F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 2);
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
