package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.common.capability.object.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface PlayerData extends INBTSerializable<CompoundNBT> {

    DebuffData getDebuffData();

    CompoundNBT writePermanentData();

    AimInfo getAimInfo();

    ReloadInfo getReloadInfo();

    ScopeData getScopeData();

    PlayerSkills getSkills();

    void setOnCooldown();

    void readPermanentData(CompoundNBT nbt);

    void tick();

    void sync();

    void syncCloneData();

    void handleLogin();
}
