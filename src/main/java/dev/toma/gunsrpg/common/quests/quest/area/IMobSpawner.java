package dev.toma.gunsrpg.common.quests.quest.area;

import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public interface IMobSpawner {

    int getWeight();

    void spawnMobsRandomly(World world, QuestArea area, QuestingGroup group);

    CompoundNBT toNbt();
}
