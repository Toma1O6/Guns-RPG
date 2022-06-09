package dev.toma.gunsrpg.ai;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        if (target == null || target.isDeadOrDying() || target.isSpectator() || target.isCreative()) return;
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
