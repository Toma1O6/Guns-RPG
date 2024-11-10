package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.api.common.ISyncRequestDispatcher;
import net.minecraft.nbt.CompoundNBT;

public interface IPlayerCapEntry {

    int getFlag();

    void toNbt(CompoundNBT nbt);

    void fromNbt(CompoundNBT nbt);

    void setSyncRequestTemplate(ISyncRequestDispatcher request);
}
