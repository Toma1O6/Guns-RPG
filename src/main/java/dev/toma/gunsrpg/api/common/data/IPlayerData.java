package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;

public interface IPlayerData {

    IAimInfo getAimInfo();

    IReloadInfo getReloadInfo();

    IJamInfo getJamInfo();

    IDebuffs getDebuffControl();

    IAttributeProvider getAttributes();

    ISkillProvider getSkillProvider();

    IPerkProvider getPerkProvider();

    IProgressData getProgressData();

    IHandState getHandState();

    IQuests getQuests();

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
