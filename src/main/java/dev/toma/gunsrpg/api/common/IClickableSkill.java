package dev.toma.gunsrpg.api.common;

import net.minecraft.entity.player.PlayerEntity;

public interface IClickableSkill {

    void clientHandleClicked();

    void clicked(PlayerEntity player);
}
