package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.init.CommonRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

public class BaseOreBlock extends OreBlock {

    public BaseOreBlock(String name, Properties properties) {
        super(properties);
        setRegistryName(name);
        CommonRegistry.registerItemBlock(this);
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return MathHelper.nextInt(RANDOM, 2, 4);
    }
}
