package dev.toma.gunsrpg.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

public class AmethystOreBlock extends BaseOreBlock {

    public AmethystOreBlock(String name, Properties properties) {
        super(name, properties);
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? MathHelper.nextInt(RANDOM, 5, 11) : 0;
    }
}
