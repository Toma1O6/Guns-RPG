package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.api.client.IProgressionDetailProvider;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT, _interface = IProgressionDetailProvider.class)
public interface IKillData extends IPointProvider, IProgressionDetailProvider {

    void addLevels(int levels);

    int getLevel();

    void advanceLevel(boolean notify);

    void onEnemyKilled(Entity enemy, ItemStack weapon);

    int getKills();

    int getRequiredKillCount();

    int getLevelLimit();
}
