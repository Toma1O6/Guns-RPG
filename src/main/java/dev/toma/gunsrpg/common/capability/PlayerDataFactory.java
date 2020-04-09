package dev.toma.gunsrpg.common.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataFactory implements PlayerData {

    private EntityPlayer player;

    public PlayerDataFactory() {
        this(null);
    }

    public PlayerDataFactory(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void sync() {

    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
