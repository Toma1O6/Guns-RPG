package dev.toma.gunsrpg.common.quests.quest.area;

import dev.toma.gunsrpg.ai.AlwaysAggroOnGoal;
import dev.toma.gunsrpg.ai.QuestPlayerSensor;
import dev.toma.gunsrpg.common.init.ModSensors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
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
import java.util.Map;
import java.util.stream.Collectors;

public class MobSpawner implements IMobSpawner {

    private final EntityType<? extends LivingEntity> entityType;
    private final int weight;
    private final int minCount;
    private final int maxCount;
    private final List<IMobSpawnProcessor> processorList;

    public MobSpawner(EntityType<? extends LivingEntity> entityType, int weight, int minCount, int maxCount, List<IMobSpawnProcessor> processorList) {
        this.entityType = entityType;
        this.weight = weight;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.processorList = processorList;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @SuppressWarnings("unchecked")
    public static MobSpawner fromNbt(CompoundNBT nbt) {
        ResourceLocation entityId = new ResourceLocation(nbt.getString("livingEntity"));
        EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) ForgeRegistries.ENTITIES.getValue(entityId);
        int weight = nbt.getInt("weight");
        int minCount = nbt.getInt("minCount");
        int maxCount = nbt.getInt("maxCount");
        ListNBT listNBT = nbt.getList("processors", Constants.NBT.TAG_COMPOUND);
        List<IMobSpawnProcessor> list = listNBT.stream().<IMobSpawnProcessor>map(inbt -> MobSpawnProcessorType.fromNbt((CompoundNBT) inbt)).collect(Collectors.toList());
        return new MobSpawner(entityType, weight, minCount, maxCount, list);
    }

    @Override
    public void spawnMobsRandomly(World world, QuestArea area, PlayerEntity attackTarget) {
        int toSpawn = minCount + world.random.nextInt(1 + maxCount - minCount);
        for (int i = 0; i < toSpawn; i++) {
            spawnMob(world, area, attackTarget);
        }
    }

    @Override
    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("livingEntity", entityType.getRegistryName().toString());
        nbt.putInt("weight", weight);
        nbt.putInt("minCount", minCount);
        nbt.putInt("maxCount", maxCount);
        ListNBT list = new ListNBT();
        processorList.stream().map(this::serializeProcessor).forEach(list::add);
        nbt.put("processors", list);
        return nbt;
    }

    @SuppressWarnings("unchecked")
    private void spawnMob(World world, QuestArea area, PlayerEntity attackTarget) {
        BlockPos pos = area.getRandomEgdePosition(world.random, world);
        LivingEntity livingEntity = entityType.create(world);
        livingEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
        IMobTargettingContext context = entity -> {
            ServerWorld serverWorld = (ServerWorld) entity.level;
            if (entity instanceof MobEntity) {
                MobEntity mob = (MobEntity) entity;
                mob.finalizeSpawn(serverWorld, world.getCurrentDifficultyAt(pos), SpawnReason.COMMAND, null, null);
                mob.setTarget(attackTarget);
                AlwaysAggroOnGoal<?> alwaysAggroOnGoal = new AlwaysAggroOnGoal<>(mob, false, attackTarget);
                mob.targetSelector.addGoal(0, alwaysAggroOnGoal);
            }
            entity.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(area.getScheme().getSize() * 4);
            Brain<?> brain = entity.getBrain();
            brain.setMemory(MemoryModuleType.UNIVERSAL_ANGER, true);
            Map<SensorType<? extends Sensor<?>>, Sensor<?>> sensors = (Map<SensorType<? extends Sensor<?>>, Sensor<?>>) brain.sensors;
            sensors.remove(SensorType.NEAREST_PLAYERS);
            SensorType<QuestPlayerSensor> sensorType = ModSensors.QUEST_PLAYER_SENSOR;
            QuestPlayerSensor questPlayerSensor = sensorType.create();
            questPlayerSensor.setTarget(attackTarget);
            sensors.put(sensorType, questPlayerSensor);
        };
        processorList.forEach(processor -> processor.processMobSpawn(livingEntity, context));
        world.addFreshEntity(livingEntity);
        context.processMobSpawn(livingEntity);
    }

    @SuppressWarnings("unchecked")
    private <P extends IMobSpawnProcessor> CompoundNBT serializeProcessor(P processor) {
        MobSpawnProcessorType<P> type = (MobSpawnProcessorType<P>) processor.getType();
        return type.toNbt(processor);
    }
}
