package dev.toma.gunsrpg.common.quests.mayor;

import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.capability.object.PlayerTraderStandings;
import dev.toma.gunsrpg.util.helper.ReputationHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class TraderStatus implements ITraderStatus, INBTSerializable<CompoundNBT> {

    private final PlayerTraderStandings.IRequestFactoryProvider provider;
    private final PlayerEntity player;
    private float reputation;
    private boolean maxedOutReputation;

    public TraderStatus(PlayerTraderStandings.IRequestFactoryProvider provider, PlayerEntity player) {
        this.provider = provider;
        this.player = player;
    }

    @Override
    public float getReputation() {
        return reputation;
    }

    @Override
    public void addReputation(float reputation) {
        float input = this.reputation + reputation;
        this.reputation = ReputationHelper.clampWithinReputationLimits(input);
        if (!maxedOutReputation && ReputationHelper.isMaxedOut(this.reputation)) {
            maxedOutReputation = true;
            ReputationHelper.awardPlayerForReputation(player);
        }
        this.provider.getRequestFactory().makeSyncRequest();
    }

    @Override
    public void onTraderAttacked() {
        reputation = Math.max(0.0F, reputation - 0.5F);
        synchronize();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("reputation", reputation);
        nbt.putBoolean("maxedOutReputation", maxedOutReputation);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        reputation = nbt.getFloat("reputation");
        maxedOutReputation = nbt.getBoolean("maxedOutReputation");
    }

    private void synchronize() {
        provider.getRequestFactory().makeSyncRequest();
    }
}
