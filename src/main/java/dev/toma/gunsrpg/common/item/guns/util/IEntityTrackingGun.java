package dev.toma.gunsrpg.common.item.guns.util;

import net.minecraft.entity.player.PlayerEntity;

public interface IEntityTrackingGun extends IAdditionalShootData {

    boolean canBeGuided(PlayerEntity player);

    int getMaxRange();

    int getLockTime();

    int getRgb(boolean locked);
}
