package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.data.IQuests;
import dev.toma.gunsrpg.common.quests.IQuest;
import dev.toma.gunsrpg.common.quests.IQuestInstance;
import net.minecraft.nbt.CompoundNBT;

import java.util.IdentityHashMap;
import java.util.Map;

public class PlayerQuests implements IQuests, IPlayerCapEntry {

    private final Map<IQuest, IQuestInstance> typeMap = new IdentityHashMap<>();
    private IClientSynchReq syncRequest = () -> {};

    @Override
    public boolean hasQuest(IQuest quest) {
        return getByType(quest) != null;
    }

    @Override
    public IQuestInstance getByType(IQuest quest) {
        return typeMap.get(quest);
    }

    @Override
    public int getFlag() {
        return DataFlags.QUESTS;
    }

    @Override
    public void toNbt(CompoundNBT nbt) {
        // TODO
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        // TODO
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        syncRequest = request;
    }
}
