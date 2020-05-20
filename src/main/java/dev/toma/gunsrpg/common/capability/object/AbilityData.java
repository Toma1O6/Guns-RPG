package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skilltree.Ability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class AbilityData {

    public final Map<Ability.Type, Ability> lockedProperties;
    public final Map<Ability.UnlockableType, Ability> lockedSkills;
    public final Map<Ability.Type, Ability> unlockedProperties;
    public final Map<Ability.UnlockableType, Ability> unlockedSkills;
    private final SkillData skillData;

    public void unlockProperty(Ability.Type type) {
        Ability a = lockedProperties.get(type);
        if(a != null) {
            lockedProperties.remove(type);
        }
        unlockedProperties.put(type, type.newAbilityInstance());
    }

    public void unlockSkill(Ability.UnlockableType type, boolean enable) {
        Ability v = lockedSkills.get(type);
        if(v != null) {
            lockedSkills.remove(type);
        }
        Ability instance = type.newAbilityInstance();
        instance.setAbilityEnabled(enable);
        unlockedSkills.put(type, instance);
    }

    public boolean hasProperty(Ability.Type type) {
        return unlockedProperties.containsKey(type);
    }

    public boolean hasSkill(Ability.UnlockableType type) {
        return unlockedSkills.containsKey(type);
    }

    public void purchaseSkill(Ability.UnlockableType type) {
        GunItem item = type.gun.get();
        SkillData.KillData data = skillData.killCount.computeIfAbsent(item, g -> new SkillData.KillData());
        data.removePoints(type.price);
        this.unlockSkill(type, false);
    }

    public boolean canPurchase(Ability.UnlockableType type) {
        GunItem it = type.gun.get();
        return skillData.killCount.computeIfAbsent(it, w -> new SkillData.KillData()).getSkillPoints() >= type.price;
    }

    public NBTTagCompound write() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList propertyList = new NBTTagList();
        for(Ability.Type type : unlockedProperties.keySet()) {
            NBTTagString tagString = new NBTTagString(type.name);
            propertyList.appendTag(tagString);
        }
        NBTTagList skillList = new NBTTagList();
        for(Map.Entry<Ability.UnlockableType, Ability> entry : unlockedSkills.entrySet()) {
            Ability.UnlockableType type = entry.getKey();
            boolean inUse = entry.getValue().enabled;
            NBTTagCompound entryNBT = new NBTTagCompound();
            entryNBT.setString("type", type.name);
            entryNBT.setBoolean("enabled", inUse);
            skillList.appendTag(entryNBT);
        }
        nbt.setTag("property", propertyList);
        nbt.setTag("skill", skillList);
        return nbt;
    }

    public void read(NBTTagCompound nbt) {
        unlockedSkills.clear();
        unlockedProperties.clear();
        NBTTagList property = nbt.hasKey("property") ? nbt.getTagList("property", Constants.NBT.TAG_STRING) : new NBTTagList();
        NBTTagList skill = nbt.hasKey("skill") ? nbt.getTagList("skill", Constants.NBT.TAG_COMPOUND) : new NBTTagList();
        for(int i = 0; i < property.tagCount(); i++) {
            Ability.Type type = Ability.findProperty(property.getStringTagAt(i));
            if(type == null) continue;
            unlockProperty(type);
        }
        for(int i = 0; i < skill.tagCount(); i++) {
            NBTTagCompound compound = skill.getCompoundTagAt(i);
            String key = compound.getString("type");
            Ability.UnlockableType type = Ability.findSkill(key);
            if(type != null) {
                this.unlockSkill(type, compound.getBoolean("enabled"));
            }
        }
    }

    public AbilityData(SkillData skillData) {
        this.skillData = skillData;
        lockedProperties = new HashMap<>();
        for (Ability.Type type : Ability.LEVEL_REWARD_TYPES) {
            lockedProperties.put(type, type.newAbilityInstance());
        }
        unlockedProperties = new HashMap<>();
        lockedSkills = new HashMap<>();
        for(Ability.UnlockableType skill : Ability.UNLOCKABLE_ABILITY_TYPES) {
            lockedSkills.put(skill, skill.newAbilityInstance());
        }
        unlockedSkills = new HashMap<>();
    }

    public void reset() {
        lockedProperties.clear();
        unlockedProperties.clear();
        lockedSkills.clear();
        unlockedSkills.clear();
        for (Ability.Type type : Ability.LEVEL_REWARD_TYPES) {
            lockedProperties.put(type, type.newAbilityInstance());
        }
        for(Ability.UnlockableType skill : Ability.UNLOCKABLE_ABILITY_TYPES) {
            lockedSkills.put(skill, skill.newAbilityInstance());
        }
    }
}
