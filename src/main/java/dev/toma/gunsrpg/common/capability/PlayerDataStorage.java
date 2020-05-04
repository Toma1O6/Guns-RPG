package dev.toma.gunsrpg.common.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PlayerDataStorage implements Capability.IStorage<PlayerData> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side, NBTBase nbt) {
        instance.deserializeNBT(nbt instanceof NBTTagCompound ? (NBTTagCompound) nbt : new NBTTagCompound());
    }
}
