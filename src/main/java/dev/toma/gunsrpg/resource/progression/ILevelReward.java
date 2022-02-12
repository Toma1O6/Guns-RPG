package dev.toma.gunsrpg.resource.progression;

import dev.toma.gunsrpg.api.common.data.IKillData;
import net.minecraft.entity.player.PlayerEntity;

public interface ILevelReward {

    void applyTo(PlayerEntity player, IKillData data);
}
