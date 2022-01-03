package dev.toma.gunsrpg.api.common.skill;

import net.minecraft.entity.player.PlayerEntity;

public interface ITickableSkill {

    void onUpdate(PlayerEntity player);
}
