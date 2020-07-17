package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.TickableSkill;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.OptionalObject;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerSkills {

    private final PlayerDataFactory data;
    private final Map<GunItem, GunData> gunKills = new HashMap<>();
    private final Map<SkillCategory, List<ISkill>> unlockedSkills = new HashMap<>();
    private int level;
    private int requiredKills = 10;
    private int kills;
    private int skillPoints;

    private Map<SkillCategory, List<TickableSkill>> tickCache;

    public PlayerSkills(PlayerDataFactory data) {
        this.data = data;
    }

    public void update() {
        if (tickCache != null) {
            for (List<TickableSkill> skillList : tickCache.values()) {
                for (TickableSkill skill : skillList) {
                    skill.onUpdate(data.getPlayer());
                }
            }
        } else createCache();
    }

    public boolean hasSkill(SkillType<?> type) {
        List<ISkill> list = unlockedSkills.get(type.category);
        if(list == null) return false;
        return ModUtils.contains(type, list, SkillType::areTypesEqual);
    }

    @SuppressWarnings("unchecked")
    public <S extends ISkill> S getSkill(SkillType<S> type) {
        SkillCategory category = type.category;
        List<ISkill> list = unlockedSkills.get(category);
        if(list == null) return null;
        for(ISkill skill : list) {
            if(type.areTypesEqual(skill)) {
                return (S) skill;
            }
        }
        return null;
    }

    public void killMob(GunItem gunItem) {
        GunData gunData = gunKills.computeIfAbsent(gunItem, it -> new GunData());
        gunData.awardKill();
        data.sync();
    }

    public void revertToDefault() {
        gunKills.clear();
        unlockedSkills.clear();
        level = 0;
        kills = 0;
        skillPoints = 0;
        clearCache();
        data.sync();
    }

    public void unlockSkill(SkillType<?> skillType) {
        unlockSkill(skillType, true);
    }

    public void unlockSkill(SkillType<?> skillType, boolean sync) {
        SkillCategory category = skillType.category;
        List<ISkill> list = unlockedSkills.computeIfAbsent(category, cat -> new ArrayList<>());
        if (!ModUtils.contains(skillType, list, SkillType::areTypesEqual)) {
            clearCache();
            list.add(skillType.instantiate());
            if (sync) data.sync();
        }
    }

    public void unlockAll() {
        level = 100;
        skillPoints = 0;
        kills = 0;
        gunKills.clear();
        for (SkillType<?> skillType : ModRegistry.SKILLS.getValuesCollection()) {
            unlockSkill(skillType, false);
        }
        clearCache();
        data.sync();
    }

    public int getLevel() {
        return level;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    public int getKills() {
        return kills;
    }

    public int getRequiredKills() {
        return requiredKills;
    }

    public Map<GunItem, GunData> getGunStats() {
        return gunKills;
    }

    public GunData getGunData(GunItem item) {
        return gunKills.computeIfAbsent(item, k -> new GunData());
    }

    public void addSkillPoints(int amount) {
        this.skillPoints = Math.max(0, skillPoints + amount);
    }

    public NBTTagCompound writeData() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("level", level);
        nbt.setInteger("requiredKills", requiredKills);
        nbt.setInteger("kills", kills);
        nbt.setInteger("skillpoints", skillPoints);
        NBTTagCompound gunData = new NBTTagCompound();
        for (Map.Entry<GunItem, GunData> entry : gunKills.entrySet()) {
            String k = entry.getKey().getRegistryName().toString();
            GunData v = entry.getValue();
            gunData.setTag(k, v.saveData());
        }
        nbt.setTag("gunData", gunData);
        NBTTagCompound skills = new NBTTagCompound();
        for (Map.Entry<SkillCategory, List<ISkill>> entry : unlockedSkills.entrySet()) {
            NBTTagList v = new NBTTagList();
            String k = entry.getKey().ordinal() + "";
            for (ISkill skill : entry.getValue()) {
                v.appendTag(skill.saveData());
            }
            skills.setTag(k, v);
        }
        nbt.setTag("skills", skills);
        return nbt;
    }

    public void readData(NBTTagCompound nbt) {
        clearCache();
        unlockedSkills.clear();
        gunKills.clear();
        level = nbt.getInteger("level");
        requiredKills = Math.max(10, nbt.getInteger("requiredKills"));
        kills = nbt.getInteger("kills");
        skillPoints = nbt.getInteger("skillpoints");
        NBTTagCompound gunData = nbt.hasKey("gunData") ? nbt.getCompoundTag("gunData") : new NBTTagCompound();
        for (String key : gunData.getKeySet()) {
            Item it = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
            if (it instanceof GunItem) {
                NBTTagCompound v = gunData.getCompoundTag(key);
                GunData data = new GunData();
                data.readData(v);
                gunKills.put((GunItem) it, data);
            }
        }
        NBTTagCompound skills = nbt.hasKey("skills") ? nbt.getCompoundTag("skills") : new NBTTagCompound();
        for (String id : skills.getKeySet()) {
            NBTTagList list = nbt.getTagList(id, Constants.NBT.TAG_COMPOUND);
            SkillCategory category = SkillCategory.values()[Integer.parseInt(id)];
            for (NBTBase nbtBase : list) {
                NBTTagCompound tagCompound = (NBTTagCompound) nbtBase;
                ResourceLocation key = new ResourceLocation(tagCompound.getString("type"));
                SkillType<?> skillType = ModRegistry.SKILLS.getValue(key);
                if (skillType != null) {
                    ISkill instance = skillType.instantiate();
                    instance.readData(tagCompound);
                    unlockedSkills.computeIfAbsent(category, c -> new ArrayList<>()).add(instance);
                }
            }
        }
    }

    private void clearCache() {
        tickCache = null;
    }

    private void createCache() {
        tickCache = new HashMap<>();
        for (SkillCategory category : SkillCategory.tickables()) {
            OptionalObject<List<ISkill>> obj = OptionalObject.of(unlockedSkills.get(category));
            if (!obj.isPresent()) continue;
            tickCache.put(category, obj.get().stream().filter(s -> s instanceof TickableSkill).map(s -> (TickableSkill) s).collect(Collectors.toList()));
        }
    }
}
