package dev.toma.gunsrpg.common.skilltree;

import dev.toma.gunsrpg.common.capability.object.SkillData;
import dev.toma.gunsrpg.common.item.guns.GunItem;

import java.util.Map;

public class EntryInstance {

    public SkillTreeEntry type;
    public boolean isObtained;

    protected EntryInstance(SkillTreeEntry entry) {
        this.type = entry;
    }

    public boolean canUnlock(Map<GunItem, SkillData.KillData> data) {
        GunItem k = type.gun;
        SkillData.KillData killData = data.computeIfAbsent(k, w -> new SkillData.KillData());
        return killData.getKillCount() >= type.requiredKillCount;
    }

    public SkillTreeEntry getType() {
        return type;
    }
}
