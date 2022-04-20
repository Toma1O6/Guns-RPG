package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.data.IQuests;
import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

public class PlayerQuests implements IQuests, IPlayerCapEntry {

    private final PlayerTraderStandings standings;
    private IClientSynchReq syncRequestFactory;

    public PlayerQuests() {
        this.standings = new PlayerTraderStandings();
    }

    @Override
    public ITraderStandings getTraderStandings() {
        return standings;
    }

    @Override
    public int getFlag() {
        return DataFlags.QUESTS;
    }

    @Override
    public void toNbt(CompoundNBT nbt) {
        nbt.put("standings", standings.serializeNBT());
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        standings.deserializeNBT(nbt.getList("standings", Constants.NBT.TAG_COMPOUND));
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        this.syncRequestFactory = request;
    }
}
