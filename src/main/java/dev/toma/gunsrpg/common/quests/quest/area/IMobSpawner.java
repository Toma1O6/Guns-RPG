package dev.toma.gunsrpg.common.quests.quest.area;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public interface IMobSpawner {

    boolean canSpawnEntity(World world);

    void spawnMobRandomly(World world, QuestArea area, PlayerEntity attackTarget);

    CompoundNBT toNbt();
}
