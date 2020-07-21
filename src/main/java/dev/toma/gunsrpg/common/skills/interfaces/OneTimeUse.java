package dev.toma.gunsrpg.common.skills.interfaces;

import net.minecraft.entity.player.EntityPlayer;

public interface OneTimeUse extends TickableSkill {

    int getCooldown();

    void setOnCooldown();

    int getMaxCooldown();

    void onUse(EntityPlayer player);
}
