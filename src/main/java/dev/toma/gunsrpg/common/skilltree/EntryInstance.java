package dev.toma.gunsrpg.common.skilltree;

import dev.toma.gunsrpg.common.item.guns.GunItem;

import java.util.Map;

public class EntryInstance {

    public SkillTreeEntry type;
    public boolean isObtained;

    protected EntryInstance(SkillTreeEntry entry) {
        this.type = entry;
    }

    public boolean canUnlock(Map<GunItem, Integer> data) {
        GunItem k = type.gun;
        return data.computeIfAbsent(k, wep -> 0) >= type.requiredKillCount;
    }

    public SkillTreeEntry getType() {
        return type;
    }
}
