package dev.toma.gunsrpg.common.skills.interfaces;

import net.minecraft.entity.player.PlayerEntity;

public interface TickableSkill {

    void onUpdate(PlayerEntity player);
}
