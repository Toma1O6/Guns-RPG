package dev.toma.gunsrpg.world.ore;

import com.google.common.base.Predicate;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class SingleOreGen extends WorldGenerator {

    private final IBlockState state;
    private final Predicate<IBlockState> predicate;

    public SingleOreGen(IBlockState state) {
        this(state, st -> st != null && st.getBlock() == Blocks.STONE && st.getValue(BlockStone.VARIANT).isNatural());
    }

    public SingleOreGen(IBlockState state, Predicate<IBlockState> predicate) {
        this.state = state;
        this.predicate = predicate;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        IBlockState state = worldIn.getBlockState(position);
        if(state.getBlock().isReplaceableOreGen(state, worldIn, position, predicate)) {
            setBlockAndNotifyAdequately(worldIn, position, this.state);
        }
        return true;
    }
}
