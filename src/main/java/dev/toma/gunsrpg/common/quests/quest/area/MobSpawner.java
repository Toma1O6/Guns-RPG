package dev.toma.gunsrpg.common.quests.quest.area;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class MobSpawner implements IMobSpawner {

    private final EntityType<? extends LivingEntity> entityType;
    private final float spawnPropability;
    private final int spawnLimit;
    private final List<IMobSpawnProcessor> processorList;

    public MobSpawner(EntityType<? extends LivingEntity> entityType, float spawnPropability, int spawnLimit, List<IMobSpawnProcessor> processorList) {
        this.entityType = entityType;
        this.spawnPropability = spawnPropability;
        this.spawnLimit = spawnLimit;
        this.processorList = processorList;
    }

    @Override
    public boolean canSpawnEntity(World world) {
        Random random = world.getRandom();
        return random.nextFloat() < spawnPropability;
    }

    @Override
    public void spawnMobRandomly(World world, IQuestArea area, PlayerEntity attackTarget) {
        int toSpawn = 1 + world.getRandom().nextInt(spawnLimit);
        for (int i = 0; i < toSpawn; i++) {
            spawnMob(world, area, attackTarget);
        }
    }

    private void spawnMob(World world, IQuestArea area, PlayerEntity attackTarget) {
        BlockPos pos = area.getRandomEgdePosition(world.random, world);
        LivingEntity entity = entityType.create(world);
        entity.setPosAndOldPos(pos.getX(), entity.getY(), entity.getZ());
        processorList.forEach(processor -> processor.processMobSpawn(entity));
        world.addFreshEntity(entity);
        if (entity instanceof MobEntity) {
            ((MobEntity) entity).setTarget(attackTarget);
        }
    }
}
