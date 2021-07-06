package dev.toma.gunsrpg.world.cap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public interface WorldDataCap extends INBTSerializable<CompoundNBT> {

    boolean isBloodmoon();

    void tick(World world);

    WorldDataFactory.WorldEvent getBloodmoonEvent();
}
