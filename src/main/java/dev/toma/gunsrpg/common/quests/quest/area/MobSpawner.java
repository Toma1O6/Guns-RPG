package dev.toma.gunsrpg.common.quests.quest.area;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    @SuppressWarnings("unchecked")
    public static MobSpawner fromNbt(CompoundNBT nbt) {
        ResourceLocation entityId = new ResourceLocation(nbt.getString("livingEntity"));
        EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) ForgeRegistries.ENTITIES.getValue(entityId);
        float spawnPropability = nbt.getFloat("spawnPropability");
        int spawnLimit = nbt.getInt("spawnLimit");
        ListNBT listNBT = nbt.getList("processors", Constants.NBT.TAG_COMPOUND);
        List<IMobSpawnProcessor> list = listNBT.stream().<IMobSpawnProcessor>map(inbt -> MobSpawnProcessorType.fromNbt((CompoundNBT) inbt)).collect(Collectors.toList());
        return new MobSpawner(entityType, spawnPropability, spawnLimit, list);
    }

    @Override
    public boolean canSpawnEntity(World world) {
        Random random = world.getRandom();
        return random.nextFloat() < spawnPropability;
    }

    @Override
    public void spawnMobRandomly(World world, QuestArea area, PlayerEntity attackTarget) {
        int toSpawn = 1 + world.getRandom().nextInt(spawnLimit);
        for (int i = 0; i < toSpawn; i++) {
            spawnMob(world, area, attackTarget);
        }
    }

    @Override
    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("livingEntity", entityType.getRegistryName().toString());
        nbt.putFloat("spawnPropability", spawnPropability);
        nbt.putInt("spawnLimit", spawnLimit);
        ListNBT list = new ListNBT();
        processorList.stream().map(this::serializeProcessor).forEach(list::add);
        nbt.put("processors", list);
        return nbt;
    }

    private void spawnMob(World world, QuestArea area, PlayerEntity attackTarget) {
        BlockPos pos = area.getRandomEgdePosition(world.random, world);
        LivingEntity entity = entityType.create(world);
        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
        processorList.forEach(processor -> processor.processMobSpawn(entity));
        world.addFreshEntity(entity);
        if (entity instanceof MobEntity) {
            MobEntity mob = (MobEntity) entity;
            mob.finalizeSpawn((ServerWorld) world, world.getCurrentDifficultyAt(pos), SpawnReason.COMMAND, null, null);
            mob.setTarget(attackTarget);
            mob.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(area.getScheme().getSize() * 3);
        }
    }

    @SuppressWarnings("unchecked")
    private <P extends IMobSpawnProcessor> CompoundNBT serializeProcessor(P processor) {
        MobSpawnProcessorType<P> type = (MobSpawnProcessorType<P>) processor.getType();
        return type.toNbt(processor);
    }
}
