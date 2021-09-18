package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;

public interface IPlayerData {

    IAimInfo getAimInfo();

    IReloadInfo getReloadInfo();

    IQuests getPlayerQuests();

    IDebuffs getDebuffControl();

    IAttributeProvider getAttributes();

    ISkills getSkills();

    IData getGenericData();

    List<IPlayerCapEntry> getSaveEntries();

    void tick();

    void sync(int flags);

    void setSyncCallback(ISynchCallback callback);

    void onSync();

    CompoundNBT toNbt(int flags);

    void fromNbt(CompoundNBT nbt, int flags);

    interface ISynchCallback {
        void onSynch();
    }
}
