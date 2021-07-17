package dev.toma.gunsrpg.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PlayerDataStorage implements Capability.IStorage<PlayerData> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<PlayerData> capability, PlayerData instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<PlayerData> capability, PlayerData instance, Direction side, INBT nbt) {
        instance.deserializeNBT(nbt instanceof CompoundNBT ? (CompoundNBT) nbt : new CompoundNBT());
    }
}
