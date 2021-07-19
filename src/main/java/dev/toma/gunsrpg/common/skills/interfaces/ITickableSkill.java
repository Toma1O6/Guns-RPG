package dev.toma.gunsrpg.common.skills.interfaces;

import net.minecraft.entity.player.PlayerEntity;

public interface ITickableSkill {

    void onUpdate(PlayerEntity player);
}
