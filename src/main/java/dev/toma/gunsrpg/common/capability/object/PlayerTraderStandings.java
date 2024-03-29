package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.quests.mayor.TraderStatus;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.util.helper.ReputationHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerTraderStandings implements ITraderStandings, INBTSerializable<ListNBT> {

    private final Map<UUID, TraderStatus> statusMap = new HashMap<>();
    private final IRequestFactoryProvider requestProvider;
    private final PlayerEntity player;

    public PlayerTraderStandings(IRequestFactoryProvider requestProvider, PlayerEntity player) {
        this.requestProvider = requestProvider;
        this.player = player;
    }

    @Override
    public ITraderStatus getStatusWithTrader(UUID traderId) {
        return statusMap.computeIfAbsent(traderId, id -> new TraderStatus(requestProvider, player));
    }

    @Override
    public void questFinished(UUID traderId, Quest<?> quest) {
        ITraderStatus status = this.getStatusWithTrader(traderId);
        ReputationHelper.awardReputationForCompletedQuest(status, quest);
        this.requestProvider.getRequestFactory().makeSyncRequest();
    }

    @Override
    public void questFailed(UUID traderId, Quest<?> quest) {
        ITraderStatus status = this.getStatusWithTrader(traderId);
        ReputationHelper.takeReputationForFailedQuest(status, quest);
        this.requestProvider.getRequestFactory().makeSyncRequest();
    }

    @Override
    public ListNBT serializeNBT() {
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
        return list;
    }

    @Override
    public void deserializeNBT(ListNBT list) {
        statusMap.clear();
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT nbt = list.getCompound(i);
            long most = nbt.getLong("uuidMost");
            long least = nbt.getLong("uuidLeast");
            UUID uuid = new UUID(most, least);
            TraderStatus status = new TraderStatus(requestProvider, player);
            status.deserializeNBT(nbt.getCompound("status"));
            statusMap.put(uuid, status);
        }
    }

    @FunctionalInterface
    public interface IRequestFactoryProvider {
        IPlayerCapEntry.IClientSynchReq getRequestFactory();
    }
}
