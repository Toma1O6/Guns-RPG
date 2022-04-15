package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class NapalmReaction implements IReaction {

    public static final IReaction NAPALM = new NapalmReaction(8.0, 0.4F);

    private final double range;
    private final float factor;

    public NapalmReaction(double range, float factor) {
        this.range = range;
        this.factor = factor;
    }

    @Override
    public void react(AbstractProjectile projectile, Vector3d impact, World world) {
        if (world.isClientSide) return;
        int irange = (int) Math.ceil(range);
        int half = irange / 2;
        Set<BlockPos> positions = new HashSet<>();
        for (int x = -half; x <= half; x++) {
            for (int z = -half; z <= half; z++) {
                int posX = (int) (impact.x + x);
                int posZ = (int) (impact.z + z);
                int posY = world.getHeight(Heightmap.Type.WORLD_SURFACE, posX, posZ);
                BlockPos pos = new BlockPos(posX, posY, posZ);
                positions.add(pos);
            }
        }
        Random random = world.random;
        positions.stream().filter(pos -> random.nextDouble() < factor && distance(pos, impact) <= range).forEach(pos -> world.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3));
    }

    private double distance(BlockPos pos, Vector3d vec) {
        double x = pos.getX() - vec.x;
        double y = pos.getY() - vec.y;
        double z = pos.getZ() - vec.z;
        return Math.sqrt(x * x + y * y + z * z);
    }
}
