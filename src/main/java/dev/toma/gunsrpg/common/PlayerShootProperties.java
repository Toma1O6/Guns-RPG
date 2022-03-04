package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class PlayerShootProperties implements IShootProps {

    private final IPlayerData data;
    private final ItemStack stack;

    public PlayerShootProperties(PlayerEntity player, ItemStack stack) {
        this.data = PlayerData.getUnsafe(player);
        this.stack = stack;
    }

    @Override
    public float getInaccuracy() {
        return data.getAimInfo().isAiming() ? 0.0F : 1.5F;
    }
}
