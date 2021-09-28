package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PlayerDataStorage implements Capability.IStorage<IPlayerData> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IPlayerData> capability, IPlayerData instance, Direction side) {
        return instance.toNbt(DataFlags.WILDCARD);
    }

    @Override
    public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, Direction side, INBT nbt) {
        instance.fromNbt(nbt instanceof CompoundNBT ? (CompoundNBT) nbt : new CompoundNBT(), DataFlags.WILDCARD);
    }
}
