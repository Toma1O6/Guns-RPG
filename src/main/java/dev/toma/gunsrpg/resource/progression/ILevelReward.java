package dev.toma.gunsrpg.resource.progression;

import net.minecraft.entity.player.PlayerEntity;

public interface ILevelReward {

    void applyTo(PlayerEntity player);
}
