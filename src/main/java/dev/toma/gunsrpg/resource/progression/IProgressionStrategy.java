package dev.toma.gunsrpg.resource.progression;

import dev.toma.gunsrpg.api.common.data.IKillData;
import net.minecraft.entity.player.PlayerEntity;

public interface IProgressionStrategy {

    int getRequiredKillCount(int level);

    void applyRewards(PlayerEntity player, IKillData data, int level);
}
