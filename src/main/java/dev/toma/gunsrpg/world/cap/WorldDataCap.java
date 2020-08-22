package dev.toma.gunsrpg.world.cap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public interface WorldDataCap extends INBTSerializable<NBTTagCompound> {

    boolean isBloodmoon();

    void tick(World world);

    WorldDataFactory.WorldEvent getBloodmoonEvent();
}
