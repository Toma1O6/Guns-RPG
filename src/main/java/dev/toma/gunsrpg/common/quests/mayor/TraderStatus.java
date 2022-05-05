package dev.toma.gunsrpg.common.quests.mayor;

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
        reputation = Math.max(0.0F, reputation - 0.5F);
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
