package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IEntityTrackingGun {

    @OnlyIn(Dist.CLIENT)
    void preShootEvent(PropertyContext context);

    boolean canBeGuided(PlayerEntity player);

    int getMaxRange();

    int getLockTime();

    int getRgb(boolean locked);
}
