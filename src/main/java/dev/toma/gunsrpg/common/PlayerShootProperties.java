package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector2f;

public class PlayerShootProperties implements IShootProps {

    private final IPlayerData data;
    private final PropertyContext context;
    private final Vector2f angle;

    public PlayerShootProperties(PlayerEntity player, PropertyContext context, float x, float y) {
        this.data = PlayerData.getUnsafe(player);
        this.context = context;
        this.angle = new Vector2f(x, y);
    }

    @Override
    public Vector2f weaponAngle() {
        return this.angle;
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
