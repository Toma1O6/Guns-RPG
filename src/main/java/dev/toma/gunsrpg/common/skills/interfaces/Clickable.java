package dev.toma.gunsrpg.common.skills.interfaces;

import net.minecraft.entity.player.EntityPlayer;

public interface Clickable {

    void clientHandleClicked();

    void clicked(EntityPlayer player);
}
