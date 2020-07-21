package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.criteria.GunCriteria;
import dev.toma.gunsrpg.common.skills.interfaces.TickableSkill;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.object.OptionalObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
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

    private int gunpowderCraftYield;

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

    public void onKillEntity(Entity entity, ItemStack stack) {
        if(!(entity instanceof IMob)) return;
        ++kills;
        if(!isMaxLevel() && kills >= requiredKills) {
            ++level;
            this.onLevelUp();
            kills = 0;
            EntityPlayer player = data.getPlayer();
            player.sendMessage(new TextComponentString("§e=====[ LEVEL UP ]====="));
            player.sendMessage(new TextComponentString("§eCurrent level: " + level));
            int count = 0;
            for(SkillType<?> type : ModRegistry.SKILLS) {
                if(!(type.getCriteria() instanceof GunCriteria) && type.levelRequirement == level) count++;
            }
            if(count > 0) player.sendMessage(new TextComponentString(String.format("§eNew skills available: %d", count)));
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, player.posX, player.posY, player.posZ, 0.75F, 1.0F));
        }
        if(stack.getItem() instanceof GunItem) {
            this.killMob((GunItem) stack.getItem());
        }
        data.sync();
    }

    public void onLevelUp() {
        if(level == 100) {
            addSkillPoints(25);
        } else if(level == 50) {
            addSkillPoints(20);
        } else if(level % 5 == 0) {
            addSkillPoints(3);
        } else addSkillPoints(1);
    }

    public void killMob(GunItem gunItem) {
        GunData gunData = gunKills.computeIfAbsent(gunItem, it -> new GunData());
        gunData.awardKill(data.getPlayer());
    }

    public boolean isMaxLevel() {
        return level == 100;
    }

    public void revertToDefault() {
        gunKills.clear();
        unlockedSkills.clear();
        level = 0;
        kills = 0;
        skillPoints = 0;
        requiredKills = 10;
        gunpowderCraftYield = SkillUtil.getGunpowderCraftAmount(data.getPlayer());
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
            ISkill skill = skillType.instantiate();
            skill.onPurchase(data.getPlayer());
            list.add(skill);
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
        for(GunItem item : ForgeRegistries.ITEMS.getValuesCollection().stream().filter(item -> item instanceof GunItem).map(item -> (GunItem) item).collect(Collectors.toList())) {
            getGunData(item).unlockAll();
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

    public void setGunpowderCraftYield(int gunpowderCraftYield) {
        this.gunpowderCraftYield = gunpowderCraftYield;
    }

    public int getGunpowderCraftYield() {
        return gunpowderCraftYield;
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
        nbt.setInteger("gunpowderYield", gunpowderCraftYield);
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
            NBTTagList list = skills.getTagList(id, Constants.NBT.TAG_COMPOUND);
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
        gunpowderCraftYield = nbt.getInteger("gunpowderYield");
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
