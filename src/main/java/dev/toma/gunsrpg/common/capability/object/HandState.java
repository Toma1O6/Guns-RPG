package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.IHandState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class HandState implements IHandState, INBTSerializable<CompoundNBT> {

    private final ISyncRequestFactory requestFactory;
    private boolean busyHands;

    public HandState(ISyncRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public void setHandsBusy() {
        busyHands = true;
        requestFactory.makeSynchronizationRequest();
    }

    @Override
    public void freeHands() {
        busyHands = false;
        requestFactory.makeSynchronizationRequest();
    }

    @Override
    public boolean areHandsBusy() {
        return busyHands;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("busyHands", busyHands);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        busyHands = nbt.getBoolean("busyHands");
    }

    @FunctionalInterface
    public interface ISyncRequestFactory {
        void makeSynchronizationRequest();
    }
}
