package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.common.capability.object.AimInfo;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.capability.object.ReloadInfo;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface PlayerData extends INBTSerializable<CompoundNBT> {

    DebuffData getDebuffData();

    CompoundNBT writePermanentData();

    AimInfo getAimInfo();

    ReloadInfo getReloadInfo();

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
