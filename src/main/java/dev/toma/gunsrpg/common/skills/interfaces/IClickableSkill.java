package dev.toma.gunsrpg.common.skills.interfaces;

import net.minecraft.entity.player.PlayerEntity;

public interface IClickableSkill {

    void clientHandleClicked();

    void clicked(PlayerEntity player);
}
