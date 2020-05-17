package dev.toma.gunsrpg.world.cap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WorldCapProvider implements ICapabilitySerializable<NBTTagCompound> {

    @CapabilityInject(WorldDataCap.class)
    public static Capability<WorldDataCap> CAP = null;
    private WorldDataCap instance = CAP.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CAP ? CAP.cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) CAP.getStorage().writeNBT(CAP, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        CAP.getStorage().readNBT(CAP, instance, null, nbt);
    }
}
