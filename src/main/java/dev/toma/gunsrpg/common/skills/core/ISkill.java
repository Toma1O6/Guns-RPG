package dev.toma.gunsrpg.common.skills.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface ISkill {

    SkillType<?> getType();

    boolean apply(EntityPlayer user);

    default void onPurchase(EntityPlayer player) {

    }

    default void onDeactivate(EntityPlayer player) {

    }

    NBTTagCompound saveData();

    void readData(NBTTagCompound nbt);
}
