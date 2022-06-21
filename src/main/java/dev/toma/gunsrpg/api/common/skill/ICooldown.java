package dev.toma.gunsrpg.api.common.skill;

import net.minecraft.entity.player.PlayerEntity;

public interface ICooldown extends ITickableSkill {

    int getCooldown();

    void setOnCooldown();

    int getMaxCooldown();

    void onUse(PlayerEntity player);
}
