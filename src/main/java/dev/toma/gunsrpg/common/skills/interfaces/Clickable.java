package dev.toma.gunsrpg.common.skills.interfaces;

import net.minecraft.entity.player.PlayerEntity;

public interface Clickable {

    void clientHandleClicked();

    void clicked(PlayerEntity player);
}
