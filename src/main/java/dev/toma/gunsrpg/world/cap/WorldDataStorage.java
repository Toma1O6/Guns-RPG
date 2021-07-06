package dev.toma.gunsrpg.world.cap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class WorldDataStorage implements Capability.IStorage<WorldDataCap> {

    @Override
    public INBT writeNBT(Capability<WorldDataCap> capability, WorldDataCap instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<WorldDataCap> capability, WorldDataCap instance, Direction side, INBT nbt) {
        instance.deserializeNBT(nbt instanceof CompoundNBT ? (CompoundNBT) nbt : new CompoundNBT());
    }
}
