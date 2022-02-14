package dev.toma.gunsrpg.api.common.data;

import net.minecraft.entity.player.PlayerEntity;

public interface IAimInfo {

    void tick(PlayerEntity player);

    boolean startedAiming();

    boolean isAiming();

    void setAiming(boolean aiming, PlayerEntity player);

    float getProgress();

    float getProgress(float deltaTime);
}
