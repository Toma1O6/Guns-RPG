package dev.toma.gunsrpg.common.quests.trader;

import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class TraderStatus implements ITraderStatus, INBTSerializable<CompoundNBT> {

    private float reputation;

    @Override
    public float getReputation() {
        return reputation;
    }

    @Override
    public void onTraderAttacked() {
        // TODO
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("reputation", reputation);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        reputation = nbt.getFloat("reputation");
    }
}
