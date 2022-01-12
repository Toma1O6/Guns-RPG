package dev.toma.gunsrpg.api.common.skill;

import net.minecraft.entity.player.ServerPlayerEntity;

public interface IClickableSkill {

    boolean canUse();

    void onSkillUsed(ServerPlayerEntity player);
}
