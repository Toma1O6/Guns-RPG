package dev.toma.gunsrpg.api.common.data;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IKillData extends IPointProvider {

    void addLevels(int levels);

    int getLevel();

    void advanceLevel(boolean notify);

    void onEnemyKilled(Entity enemy, ItemStack weapon);

    int getKills();

    int getRequiredKillCount();

    int getLevelLimit();
}
