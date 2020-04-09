package dev.toma.gunsrpg.common.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface PlayerData extends INBTSerializable<NBTTagCompound> {

    void sync();
}
