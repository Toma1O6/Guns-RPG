package dev.toma.gunsrpg.world.cap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class WorldDataStorage implements Capability.IStorage<WorldDataCap> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<WorldDataCap> capability, WorldDataCap instance, EnumFacing side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<WorldDataCap> capability, WorldDataCap instance, EnumFacing side, NBTBase nbt) {
        instance.deserializeNBT(nbt instanceof NBTTagCompound ? (NBTTagCompound) nbt : new NBTTagCompound());
    }
}
