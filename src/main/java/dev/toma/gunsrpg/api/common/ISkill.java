package dev.toma.gunsrpg.api.common;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public interface ISkill {

    SkillType<?> getType();

    boolean canApply(PlayerEntity user);

    default void onPurchase(PlayerEntity player) {
    }

    default void onDeactivate(PlayerEntity player) {
    }

    CompoundNBT saveData();

    void readData(CompoundNBT nbt);
}
