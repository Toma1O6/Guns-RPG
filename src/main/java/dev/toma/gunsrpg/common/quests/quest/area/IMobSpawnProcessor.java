package dev.toma.gunsrpg.common.quests.quest.area;

import net.minecraft.entity.LivingEntity;

public interface IMobSpawnProcessor {

    MobSpawnProcessorType<?> getType();

    void processMobSpawn(LivingEntity entity, IMobTargettingContext targettingContext);
}
