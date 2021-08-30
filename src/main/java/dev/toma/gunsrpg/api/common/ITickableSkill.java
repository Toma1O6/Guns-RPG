package dev.toma.gunsrpg.api.common;

import net.minecraft.entity.player.PlayerEntity;

public interface ITickableSkill {

    void onUpdate(PlayerEntity player);
}
