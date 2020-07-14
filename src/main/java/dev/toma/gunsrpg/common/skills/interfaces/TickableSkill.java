package dev.toma.gunsrpg.common.skills.interfaces;

import net.minecraft.entity.player.EntityPlayer;

public interface TickableSkill {

    void onUpdate(EntityPlayer player);
}
