package dev.toma.gunsrpg.common.quests.quest.area;

import net.minecraft.entity.LivingEntity;

@FunctionalInterface
public interface IMobTargettingContext {

    void processMobSpawn(LivingEntity entity);
}
