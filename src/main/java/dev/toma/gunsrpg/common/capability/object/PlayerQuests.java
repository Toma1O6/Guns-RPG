package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.data.IQuests;
import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import java.util.Optional;

public class PlayerQuests implements IQuests, IPlayerCapEntry {

    private final PlayerEntity player;
    private final PlayerTraderStandings standings;
    private Quest<?> activeQuest;
    private IClientSynchReq syncRequestFactory;

    public PlayerQuests(PlayerEntity player) {
        this.player = player;
        this.standings = new PlayerTraderStandings(() -> syncRequestFactory, player);
    }

    @Override
    public ITraderStandings getTraderStandings() {
        return standings;
    }

    @Override
    public Optional<Quest<?>> getActiveQuest() {
        return Optional.ofNullable(activeQuest);
    }

    @Override
    public void assignQuest(Quest<?> quest) {
        setActiveQuest(quest);
    }

    @Override
    public void clearActiveQuest() {
        setActiveQuest(null);
    }

    private void setActiveQuest(Quest<?> quest) {
        this.activeQuest = quest;
        this.syncRequestFactory.makeSyncRequest();
    }

    @Override
    public int getFlag() {
        return DataFlags.QUESTS;
    }

    @Override
    public void toNbt(CompoundNBT nbt) {
        nbt.put("standings", standings.serializeNBT());
        if (activeQuest != null) {
            nbt.put("quest", activeQuest.serialize());
        }
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        standings.deserializeNBT(nbt.getList("standings", Constants.NBT.TAG_COMPOUND));
        activeQuest = nbt.contains("quest") ? Quest.fromNbt(nbt.getCompound("quest")) : null;
        this.getActiveQuest().ifPresent(quest -> quest.assign(player));
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        this.syncRequestFactory = request;
    }
}
