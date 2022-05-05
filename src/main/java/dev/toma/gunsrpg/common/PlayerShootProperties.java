package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerShootProperties implements IShootProps {

    private final IPlayerData data;
    private final PropertyContext context;

    public PlayerShootProperties(PlayerEntity player, PropertyContext context) {
        this.data = PlayerData.getUnsafe(player);
        this.context = context;
    }

    @Override
    public float getInaccuracy() {
        return data.getAimInfo().isAiming() ? 0.0F : 1.5F;
    }

    @Override
    public PropertyContext getExtraData() {
        return context;
    }
}
