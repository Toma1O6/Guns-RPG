package dev.toma.gunsrpg.api.common.skill;

import net.minecraft.entity.player.PlayerEntity;

public interface IClickableSkill {

    void clientHandleClicked();

    void clicked(PlayerEntity player);
}
