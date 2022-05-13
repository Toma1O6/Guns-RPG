package dev.toma.gunsrpg.common.quests.quest.area;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IMobSpawner {

    boolean canSpawnEntity(World world);

    void spawnMobRandomly(World world, IQuestArea area, PlayerEntity attackTarget);
}
