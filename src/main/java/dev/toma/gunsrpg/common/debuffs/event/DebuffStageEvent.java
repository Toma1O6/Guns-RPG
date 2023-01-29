package dev.toma.gunsrpg.common.debuffs.event;

import net.minecraft.entity.player.PlayerEntity;

public interface DebuffStageEvent {

    void apply(PlayerEntity player);

    DebuffStageEventType<?> getType();
}
