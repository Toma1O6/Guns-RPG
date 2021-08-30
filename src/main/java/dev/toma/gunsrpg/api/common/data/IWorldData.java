package dev.toma.gunsrpg.api.common.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public interface IWorldData extends INBTSerializable<CompoundNBT> {

    boolean isBloodmoon();

    void tick(World world);
}
