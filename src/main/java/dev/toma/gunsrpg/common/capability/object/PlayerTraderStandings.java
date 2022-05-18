package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.quests.mayor.TraderStatus;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerTraderStandings implements ITraderStandings, INBTSerializable<ListNBT> {

    private final Map<UUID, TraderStatus> statusMap = new HashMap<>();

    @Override
    public ITraderStatus getStatusWithTrader(UUID traderId) {
        return statusMap.computeIfAbsent(traderId, id -> new TraderStatus());
    }

    @Override
    public void questFinished(UUID traderId, Quest<?> quest) {
        // TODO
    }

    @Override
    public void questFailed(UUID traderId, Quest<?> quest) {
        // TODO
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
            TraderStatus status = new TraderStatus();
            status.deserializeNBT(nbt.getCompound("status"));
            statusMap.put(uuid, status);
        }
    }
}
