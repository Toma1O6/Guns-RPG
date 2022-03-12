package dev.toma.gunsrpg.world.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class UnderTopLayerPlacement extends Placement<NoPlacementConfig> {

    private final double spawnChance;

    public UnderTopLayerPlacement(double spawnChance) {
        super(NoPlacementConfig.CODEC);
        this.spawnChance = spawnChance;
    }

    @Override
    public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random random, NoPlacementConfig config, BlockPos chunkStartPos) {
        if (random.nextFloat() >= 0.005F) {
            return Stream.empty();
        }
        int x = chunkStartPos.getX() + random.nextInt(16);
        int z = chunkStartPos.getZ() + random.nextInt(16);
        int y = helper.getHeight(Heightmap.Type.WORLD_SURFACE, x, z) - 5;
        return Stream.of(new BlockPos(x, y, z));
    }
}
