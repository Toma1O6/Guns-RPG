package dev.toma.gunsrpg.api.common.data;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IKillData {

    int getLevel();

    void advanceLevel(boolean notify);

    int getPoints();

    void awardPoints(int points);

    void onEnemyKilled(Entity enemy, ItemStack weapon);

    int getKills();

    int getRequiredKillCount();

    int getLevelLimit();
}
