package dev.toma.gunsrpg.integration.questing.area.spawner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.ai.AlwaysAggroOnGoal;
import dev.toma.gunsrpg.ai.QuestPlayerSensor;
import dev.toma.gunsrpg.common.init.ModSensors;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.questing.common.component.area.instance.Area;
import dev.toma.questing.common.component.area.spawner.EntitySpawner;
import dev.toma.questing.common.component.area.spawner.Spawner;
import dev.toma.questing.common.component.area.spawner.SpawnerType;
import dev.toma.questing.common.component.area.spawner.processor.SpawnerProcessor;
import dev.toma.questing.common.component.area.spawner.processor.SpawnerProcessorType;
import dev.toma.questing.common.party.Party;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.utils.Codecs;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class TargettedEntitySpawner extends EntitySpawner {

    public static final Codec<TargettedEntitySpawner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codecs.enumCodec(SpawnMode.class).optionalFieldOf("spawnMode", SpawnMode.GROUND).forGetter(t -> t.spawnMode),
            ResourceLocation.CODEC.flatXmap(location -> {
                if (!ForgeRegistries.ENTITIES.containsKey(location)) {
                    return DataResult.error("Unknown entity " + location);
                }
                return DataResult.success(ForgeRegistries.ENTITIES.getValue(location));
            }, type -> type == null ? DataResult.error("Entity type is null") : DataResult.success(type.getRegistryName()))
                    .fieldOf("entity").forGetter(EntitySpawner::getEntity),
            Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("min", 1).forGetter(EntitySpawner::getMinCount),
            Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("max", 1).forGetter(EntitySpawner::getMaxCount),
            SpawnerProcessorType.CODEC.listOf().optionalFieldOf("processors", Collections.emptyList()).forGetter(t -> t.processors)
    ).apply(instance, TargettedEntitySpawner::new));

    public TargettedEntitySpawner(SpawnMode spawnMode, EntityType<?> entity, int minCount, int maxCount, List<SpawnerProcessor> processors) {
        super(spawnMode, entity, minCount, maxCount, processors);
    }

    @Override
    public SpawnerType<?> getType() {
        return QuestRegistry.ENTITY_SPAWNER;
    }

    @Override
    public Spawner copy() {
        return new TargettedEntitySpawner(spawnMode, entity, this.getMinCount(), this.getMaxCount(), this.processors);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void entityAddedToWorld(World world, Area area, Quest quest, Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity) entity;
        Party party = quest.getParty();
        if (world.isClientSide)
            return;
        BlockPos pos = entity.blockPosition();
        this.getClosestPlayer(party, livingEntity).ifPresent(player -> {
            ServerWorld serverWorld = (ServerWorld) world;
            if (livingEntity instanceof MobEntity) {
                MobEntity mob = (MobEntity) livingEntity;
                mob.finalizeSpawn(serverWorld, world.getCurrentDifficultyAt(pos), SpawnReason.NATURAL, null, null);
                mob.setTarget(player);
                AlwaysAggroOnGoal<?> alwaysAggroOnGoal = new AlwaysAggroOnGoal<>(mob, false, player);
                mob.targetSelector.addGoal(0, alwaysAggroOnGoal);
            }
            livingEntity.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(256.0D);
            Brain<?> brain = livingEntity.getBrain();
            brain.setMemory(MemoryModuleType.UNIVERSAL_ANGER, true);
            Map<SensorType<? extends Sensor<?>>, Sensor<?>> sensors = (Map<SensorType<? extends Sensor<?>>, Sensor<?>>) brain.sensors;
            sensors.remove(SensorType.NEAREST_PLAYERS);
            SensorType<QuestPlayerSensor> sensorType = ModSensors.QUEST_PLAYER_SENSOR;
            QuestPlayerSensor questPlayerSensor = sensorType.create();
            questPlayerSensor.setTarget(player);
            sensors.put(sensorType, questPlayerSensor);
        });
    }

    private Optional<PlayerEntity> getClosestPlayer(Party party, LivingEntity entity) {
        return party.getMembers().stream()
                .map(uuid -> entity.level.getPlayerByUUID(uuid))
                .filter(Objects::nonNull)
                .min((p1, p2) -> {
                    int a = (int) entity.distanceToSqr(p1);
                    int b = (int) entity.distanceToSqr(p2);
                    return a - b;
                });
    }
}
