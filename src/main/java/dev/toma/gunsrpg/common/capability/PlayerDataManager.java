package dev.toma.gunsrpg.common.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerDataManager implements ICapabilitySerializable<NBTTagCompound> {

    @CapabilityInject(PlayerData.class)
    public static final Capability<PlayerData> CAPABILITY = null;
    private PlayerData instance;

    public PlayerDataManager(EntityPlayer player) {
        instance = new PlayerDataFactory();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY ? CAPABILITY.cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) CAPABILITY.getStorage().writeNBT(CAPABILITY, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance, null, nbt);
    }
}
