package dev.toma.gunsrpg.common.quests.quest.area;

import dev.toma.gunsrpg.util.math.Vec2i;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import java.util.Random;

public class QuestArea implements IQuestArea {

    private final QuestAreaScheme scheme;
    private final BlockPos pos;
    private final Vec2i cornerA;
    private final Vec2i cornerB;

    public QuestArea(QuestAreaScheme scheme, BlockPos pos) {
        this.scheme = scheme;
        this.pos = pos;

        int radius = scheme.getSize();
        this.cornerA = new Vec2i(pos.getX() - radius, pos.getZ() - radius);
        this.cornerB = new Vec2i(pos.getX() + radius + 1, pos.getZ() + radius + 1);
    }

    @Override
    public QuestAreaScheme getScheme() {
        return scheme;
    }

    @Override
    public BlockPos getAreaCenter() {
        return pos;
    }

    @Override
    public BlockPos getRandomEgdePosition(Random random, World world) {
        int mode = random.nextInt(4);
        int minX = cornerA.x();
        int minZ = cornerA.y();
        int maxX = cornerB.x();
        int maxZ = cornerB.y();
        switch (mode) {
            case 0: {
                int z = minZ + random.nextInt(maxZ - minZ + 1);
                int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, minX, z);
                return new BlockPos(minX, y, z);
            }
            case 1: {
                int x = minX + random.nextInt(maxX - minX + 1);
                int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, minZ);
                return new BlockPos(x, y, minZ);
            }
            case 2: {
                int z = minZ + random.nextInt(maxZ - minZ + 1);
                int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, maxX, z);
                return new BlockPos(maxX, y, z);
            }
            case 3:
            default: {
                int x = minX + random.nextInt(maxX - minX + 1);
                int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, maxZ);
                return new BlockPos(minX, y, maxZ);
            }
        }
    }

    @Override
    public boolean isInArea(Entity entity) {
        int x = (int) entity.getX();
        int z = (int) entity.getZ();
        int yDiff = (int) (Math.abs(entity.getY() - pos.getY()));
        return yDiff <= scheme.getSize() && x >= cornerA.x() && z >= cornerA.y() && x <= cornerB.x() && z <= cornerB.y();
    }
}
