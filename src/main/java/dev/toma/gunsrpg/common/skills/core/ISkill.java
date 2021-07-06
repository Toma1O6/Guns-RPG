package dev.toma.gunsrpg.common.skills.core;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public interface ISkill {

    SkillType<?> getType();

    boolean apply(PlayerEntity user);

    default void onPurchase(PlayerEntity player) {
    }

    default void onDeactivate(PlayerEntity player) {
    }

    CompoundNBT saveData();

    void readData(CompoundNBT nbt);
}
