package dev.toma.gunsrpg.common.quests.mayor;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.capability.object.PlayerTraderStandings;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.util.helper.ReputationHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
        ReputationStatus previousStatus = ReputationStatus.getStatus(this.reputation);
        float input = this.reputation + reputation;
        this.reputation = ReputationHelper.clampWithinReputationLimits(input);
        ReputationStatus currentStatus = ReputationStatus.getStatus(this.reputation);
        this.sendReputationStatusUpdate(previousStatus, currentStatus);
        if (!maxedOutReputation && ReputationHelper.isMaxedOut(this.reputation)) {
            maxedOutReputation = true;
            ReputationHelper.awardPlayerForReputation(player);
        }
        this.synchronize();
        GunsRPG.log.debug(QuestSystem.MARKER, "Updated {}'s reputation to {}", player.getName().getString(), this.reputation);
    }

    @Override
    public void onTraderAttacked() {
        this.addReputation(-0.5F);
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

    private void sendReputationStatusUpdate(ReputationStatus prev, ReputationStatus current) {
        if (prev == current || player.level.isClientSide) {
            return;
        }
        int oldStatusValue = prev.ordinal();
        int statusValue = current.ordinal();
        ITextComponent status = current.getStatusDescriptor();
        ITextComponent message = new TranslationTextComponent(oldStatusValue < statusValue ? "quest.reputation.upgraded" : "quest.reputation.lost", status);
        player.sendMessage(message, Util.NIL_UUID);
    }
}
