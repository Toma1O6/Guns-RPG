package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.quests.mayor.TraderStatus;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.util.helper.ReputationHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerTraderStandings implements ITraderStandings, IPlayerCapEntry {

    private final Map<UUID, TraderStatus> statusMap = new HashMap<>();
    private final PlayerEntity player;
    private IClientSynchReq synchReq;

    public PlayerTraderStandings(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public int getFlag() {
        return DataFlags.QUESTS;
    }

    @Override
    public void toNbt(CompoundNBT tag) {
        ListNBT list = new ListNBT();
        for (Map.Entry<UUID, TraderStatus> entry : statusMap.entrySet()) {
            UUID uuid = entry.getKey();
            TraderStatus status = entry.getValue();
            CompoundNBT nbt = new CompoundNBT();
            nbt.putLong("uuidMost", uuid.getMostSignificantBits());
            nbt.putLong("uuidLeast", uuid.getLeastSignificantBits());
            nbt.put("status", status.serializeNBT());
            list.add(nbt);
        }
        tag.put("reputation", list);
    }

    @Override
    public void fromNbt(CompoundNBT tag) {
        ListNBT listNBT = tag.getList("reputation", Constants.NBT.TAG_COMPOUND);
        statusMap.clear();
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundNBT nbt = listNBT.getCompound(i);
            long most = nbt.getLong("uuidMost");
            long least = nbt.getLong("uuidLeast");
            UUID uuid = new UUID(most, least);
            TraderStatus status = new TraderStatus(this::synchronize, player);
            status.deserializeNBT(nbt.getCompound("status"));
            statusMap.put(uuid, status);
        }
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        this.synchReq = request;
    }

    @Override
    public ITraderStatus getStatusWithTrader(UUID traderId) {
        return statusMap.computeIfAbsent(traderId, id -> new TraderStatus(this::synchronize, player));
    }

    @Override
    public void questFinished(UUID traderId, Quest<?> quest) {
        ITraderStatus status = this.getStatusWithTrader(traderId);
        ReputationHelper.awardReputationForCompletedQuest(status, quest);
        this.synchronize();
    }

    @Override
    public void questFailed(UUID traderId, Quest<?> quest) {
        ITraderStatus status = this.getStatusWithTrader(traderId);
        ReputationHelper.takeReputationForFailedQuest(status, quest);
        this.synchronize();
    }

    private void synchronize() {
        if (this.synchReq != null) {
            this.synchReq.makeSyncRequest();
        }
    }
}
