package com.wf.firearms.bloodmoon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/** 主世界血月状态（自然周期 + 指令强制）。 */
public final class BloodmoonSaveData extends SavedData {
    private static final String ID = "gunsrpg_bloodmoon";

    private boolean active;
    private boolean wasActive;
    private boolean forced;

    public BloodmoonSaveData() {}

    public static BloodmoonSaveData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                BloodmoonSaveData::load, BloodmoonSaveData::new, ID);
    }

    private static BloodmoonSaveData load(CompoundTag tag) {
        BloodmoonSaveData data = new BloodmoonSaveData();
        data.active = tag.getBoolean("active");
        data.wasActive = tag.getBoolean("wasActive");
        data.forced = tag.getBoolean("forced");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("active", active);
        tag.putBoolean("wasActive", wasActive);
        tag.putBoolean("forced", forced);
        return tag;
    }

    public boolean isActive() {
        return active;
    }

    public boolean wasActive() {
        return wasActive;
    }

    public boolean isForced() {
        return forced;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
        setDirty();
    }

    void setActive(boolean active) {
        this.active = active;
        setDirty();
    }

    void setWasActive(boolean wasActive) {
        this.wasActive = wasActive;
        setDirty();
    }
}
