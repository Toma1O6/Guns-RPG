package dev.toma.gunsrpg.common.quests.quest.area;

import dev.toma.gunsrpg.util.math.Vec2i;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class QuestArea {

    public static final ITextComponent STAY_IN_AREA = new TranslationTextComponent("quest.stay_in_area");
    private final QuestAreaScheme scheme;
    private final BlockPos pos;
    private final Vec2i cornerA;
    private final Vec2i cornerB;
    private final ITextComponent descriptor;
    private int spawnDelay;

    private Collection<ParticleEntry> edgePositions;

    public QuestArea(QuestAreaScheme scheme, BlockPos pos) {
        this.scheme = scheme;
        this.pos = pos;
        this.spawnDelay = scheme.getSpawnInterval();

        int radius = scheme.getSize();
        this.cornerA = new Vec2i(pos.getX() - radius, pos.getZ() - radius);
        this.cornerB = new Vec2i(pos.getX() + radius + 1, pos.getZ() + radius + 1);
        this.descriptor = new StringTextComponent(String.format("[%d, %d]", pos.getX(), pos.getZ()));
    }

    public static QuestArea fromNbt(QuestAreaScheme scheme, CompoundNBT nbt) {
        BlockPos pos = NBTUtil.readBlockPos(nbt.getCompound("pos"));
        int spawnDelay = nbt.getInt("spawnDelay");
        QuestArea area = new QuestArea(scheme, pos);
        area.spawnDelay = spawnDelay;
        return area;
    }

    public void tickArea(World world, PlayerEntity player) {
        if (!world.isClientSide) {
            if (--spawnDelay < 0) {
                spawnDelay = scheme.getSpawnInterval();
                List<IMobSpawner> spawners = scheme.getMobSpawnerList();
                for (IMobSpawner spawner : spawners) {
                    if (spawner.canSpawnEntity(world)) {
                        spawner.spawnMobRandomly(world, this, player);
                    }
                }
            }
        } else {
            if (edgePositions == null) {
                fillEdgePositions(world);
            }
            edgePositions.forEach(particle -> particle.makeParticles(world));
        }
    }

    public QuestAreaScheme getScheme() {
        return scheme;
    }

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

    public boolean isInArea(Entity entity) {
        double x = entity.getX();
        double z = entity.getZ();
        return isInArea(x, z);
    }

    public boolean isInArea(double x, double z) {
        return x >= cornerA.x() && z >= cornerA.y() && x <= cornerB.x() && z <= cornerB.y();
    }

    public double getDistance(Entity entity) {
        double x = pos.getX() - entity.getX();
        double z = pos.getZ() - entity.getZ();
        return Math.sqrt(x * x + z * z);
    }

    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("pos", NBTUtil.writeBlockPos(pos));
        nbt.putInt("spawnDelay", spawnDelay);
        return nbt;
    }

    public ITextComponent getDescriptor() {
        return descriptor;
    }

    private void fillEdgePositions(World world) {
        edgePositions = new ArrayList<>();
        int minX = cornerA.x();
        int minZ = cornerA.y();
        int maxX = cornerB.x();
        int maxZ = cornerB.y();
        edgePositions.add(ParticleEntry.corner(new Vector3d(minX, height(world, minX, minZ), minZ)));
        edgePositions.add(ParticleEntry.corner(new Vector3d(minX, height(world, minX, maxZ), maxZ)));
        edgePositions.add(ParticleEntry.corner(new Vector3d(maxX, height(world, maxX, minZ), minZ)));
        edgePositions.add(ParticleEntry.corner(new Vector3d(maxX, height(world, maxX, maxZ), maxZ)));

        for (int i = minX + 1; i < maxX; i++) {
            edgePositions.add(ParticleEntry.edge(new Vector3d(i, height(world, i, minZ), minZ)));
            edgePositions.add(ParticleEntry.edge(new Vector3d(i, height(world, i, maxZ), maxZ)));
        }
        for (int i = minZ + 1; i < maxZ; i++) {
            edgePositions.add(ParticleEntry.edge(new Vector3d(minX, height(world, minX, i), i)));
            edgePositions.add(ParticleEntry.edge(new Vector3d(maxX, height(world, maxX, i), i)));
        }
    }

    private int height(World world, int x, int z) {
        return world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z);
    }

    private static final class ParticleEntry {

        private final IParticleData data;
        private final Vector3d vec;
        private final float power;

        public ParticleEntry(IParticleData data, Vector3d vec, float power) {
            this.data = data;
            this.vec = vec;
            this.power = power;
        }

        public static ParticleEntry corner(Vector3d vec) {
            return new ParticleEntry(ParticleTypes.CLOUD, vec, 0.5F);
        }

        public static ParticleEntry edge(Vector3d vec) {
            return new ParticleEntry(ParticleTypes.CLOUD, vec, 0.05F);
        }

        public void makeParticles(World world) {
            world.addParticle(data, true, vec.x + 0.5, vec.y, vec.z + 0.5, 0.0, power, 0.0);
        }
    }
}
