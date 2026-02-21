package dev.toma.gunsrpg.common.quests.quest.area;

import net.minecraft.entity.MobEntity;

@FunctionalInterface
public interface IMobTargettingContext {

    void processMobSpawn(MobEntity entity);
}
