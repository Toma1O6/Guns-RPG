package dev.toma.gunsrpg.api.common.data;

import net.minecraft.nbt.CompoundNBT;

public interface IPlayerCapEntry {

    int getFlag();

    void toNbt(CompoundNBT nbt);

    void fromNbt(CompoundNBT nbt);

    void setClientSynch(IClientSynchReq request);

    interface IClientSynchReq {
        void makeSyncRequest();
    }
}
