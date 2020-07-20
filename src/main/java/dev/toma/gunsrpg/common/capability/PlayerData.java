package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.common.capability.object.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface PlayerData extends INBTSerializable<NBTTagCompound> {

    DebuffData getDebuffData();

    NBTTagCompound writePermanentData();

    AimInfo getAimInfo();

    ReloadInfo getReloadInfo();

    ScopeData getScopeData();

    PlayerSkills getSkills();

    void setShooting(boolean shooting);

    boolean isShooting();

    void readPermanentData(NBTTagCompound nbt);

    void tick();

    void sync();

    void syncCloneData();

    void handleLogin();
}
