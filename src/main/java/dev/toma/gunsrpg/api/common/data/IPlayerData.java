package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerData extends INBTSerializable<CompoundNBT> {

    IAimInfo getAimInfo();

    IReloadInfo getReloadInfo();

    IQuests getPlayerQuests();

    IDebuffs getDebuffControl();

    PlayerSkills getSkills();

    void tick();

    void sync();

    void handleLogin();

    void setSyncCallback(ISynchCallback callback);

    void onSync();

    interface ISynchCallback {
        void onSynch();
    }
}
