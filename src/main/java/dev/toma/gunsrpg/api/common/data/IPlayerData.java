package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerData extends INBTSerializable<CompoundNBT> {

    DebuffData getDebuffData();

    CompoundNBT writePermanentData();

    IAimInfo getAimInfo();

    IReloadInfo getReloadInfo();

    PlayerSkills getSkills();

    void setOnCooldown();

    void readPermanentData(CompoundNBT nbt);

    void tick();

    void sync();

    void handleLogin();

    void setSyncCallback(ISynchCallback callback);

    void onSync();

    interface ISynchCallback {
        void onSynch();
    }
}
