package dev.toma.gunsrpg.common.quests.quest.area;

import net.minecraft.entity.MobEntity;

public interface IMobSpawnProcessor {

    MobSpawnProcessorType<?> getType();

    void processMobSpawn(MobEntity entity, IMobTargettingContext targettingContext);
}
