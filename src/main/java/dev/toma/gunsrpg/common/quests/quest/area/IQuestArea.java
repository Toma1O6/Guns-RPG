package dev.toma.gunsrpg.common.quests.quest.area;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public interface IQuestArea {

    QuestAreaScheme getScheme();

    BlockPos getAreaCenter();

    BlockPos getRandomEgdePosition(Random random, World world);

    boolean isInArea(Entity entity);
}
