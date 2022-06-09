package dev.toma.gunsrpg.ai;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.stream.Collectors;

public class QuestPlayerSensor extends Sensor<LivingEntity> {

    private List<PlayerEntity> targetList;
    private PlayerEntity target;

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
    }

    @Override
    protected void doTick(ServerWorld world, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();
        if (target == null || target.isDeadOrDying() || target.isSpectator() || target.isCreative()) {
            List<PlayerEntity> list = world.players().stream().filter(EntityPredicates.NO_SPECTATORS).filter((player) -> entity.closerThan(player, 16.0D)).sorted(Comparator.comparingDouble(entity::distanceToSqr)).collect(Collectors.toList());
            brain.setMemory(MemoryModuleType.NEAREST_PLAYERS, list);
            List<PlayerEntity> list1 = list.stream().filter((player) -> isEntityTargetable(entity, player)).collect(Collectors.toList());
            brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, list1.isEmpty() ? null : list1.get(0));
            Optional<PlayerEntity> optional = list1.stream().filter(EntityPredicates.ATTACK_ALLOWED).findFirst();
            brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, optional);
            return;
        }
        brain.setMemory(MemoryModuleType.ANGRY_AT, target.getUUID());
        brain.setMemory(MemoryModuleType.NEAREST_PLAYERS, targetList);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, target);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, Optional.of(target));
    }

    public void setTarget(PlayerEntity target) {
        this.target = target;
        this.targetList = Collections.singletonList(target);
    }
}
