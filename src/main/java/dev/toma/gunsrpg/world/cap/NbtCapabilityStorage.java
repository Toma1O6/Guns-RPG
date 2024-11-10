package dev.toma.gunsrpg.world.cap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public final class NbtCapabilityStorage<T extends INBTSerializable<CompoundNBT>> implements Capability.IStorage<T> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<T> capability, T t, Direction direction) {
        return t.serializeNBT();
    }

    @Override
    public void readNBT(Capability<T> capability, T t, Direction direction, INBT inbt) {
        t.deserializeNBT(inbt instanceof CompoundNBT ? (CompoundNBT) inbt : new CompoundNBT());
    }
}
