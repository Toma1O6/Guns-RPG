package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.criteria.GunCriteria;
import dev.toma.gunsrpg.common.skills.interfaces.TickableSkill;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketNewSkills;
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
import net.minecraft.util.text.TextFormatting;
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
    private int bonemealCraftYield;
    public int poisonResistance;
    public int infectionResistance;
    public int brokenBoneResistance;
    public int bleedResistance;
    public int extraDamage;
    public float instantKillChance;
    public float axeMiningSpeed;
    public float pickaxeMiningSpeed;
    public float shovelMiningSpeed;
    public float acrobaticsFallResistance;
    public float acrobaticsExplosionResistance;
    public float lightHunterMovementSpeed;
    public float agilitySpeed;
    public float poisonChance;
    public float bleedChance;
    public float infectionChance;
    public float brokenBoneChance;

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

    public Map<SkillCategory, List<ISkill>> getUnlockedSkills() {
        return unlockedSkills;
    }

    public void onKillEntity(Entity entity, ItemStack stack) {
        if(!(entity instanceof IMob)) return;
        ++kills;
        if(!isMaxLevel() && kills >= requiredKills) {
            nextLevel(true);
        }
        if(stack.getItem() instanceof GunItem) {
            this.killMob((GunItem) stack.getItem());
        }
        data.sync();
    }

    public void nextLevel(boolean notify) {
        ++level;
        awardPoints();
        updateRequiredKills();
        kills = 0;
        if(notify) {
            EntityPlayer player = data.getPlayer();
            player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "=====[ LEVEL UP ]====="));
            player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Current level: " + level));
            int count = 0;
            List<SkillType<?>> unlockedSkills = new ArrayList<>();
            for(SkillType<?> type : ModRegistry.SKILLS) {
                if(!(type.getCriteria() instanceof GunCriteria) && type.levelRequirement == level) {
                    count++;
                    unlockedSkills.add(type);
                }
            }
            if(count > 0) {
                player.sendMessage(new TextComponentString(String.format(TextFormatting.YELLOW + "New skills available: %d", count)));
                NetworkManager.toClient((EntityPlayerMP) player, new CPacketNewSkills(unlockedSkills));
            }
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, player.posX, player.posY, player.posZ, 0.75F, 1.0F));
        }
    }

    public void awardPoints() {
        if(level == 100) {
            addSkillPoints(25);
            data.getPlayer().addItemStackToInventory(new ItemStack(ModRegistry.GRPGItems.GOLD_EGG_SHARD));
        } else if(level == 50) {
            addSkillPoints(20);
        } else if(level % 10 == 0) {
            addSkillPoints(5);
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
        for(Map.Entry<SkillCategory, List<ISkill>> entry : unlockedSkills.entrySet()) {
            for(ISkill skill : entry.getValue()) {
                skill.onDeactivate(data.getPlayer());
            }
        }
        unlockedSkills.clear();
        level = 0;
        kills = 0;
        skillPoints = 0;
        requiredKills = 10;
        gunpowderCraftYield = SkillUtil.getGunpowderCraftAmount(data.getPlayer());
        bonemealCraftYield = 0;
        poisonResistance = 0;
        infectionResistance = 0;
        brokenBoneResistance = 0;
        bleedResistance = 0;
        extraDamage = 0;
        instantKillChance = 0;
        axeMiningSpeed = 0.0F;
        shovelMiningSpeed = 0.0F;
        pickaxeMiningSpeed = 0.0F;
        acrobaticsFallResistance = 0;
        acrobaticsExplosionResistance = 0;
        lightHunterMovementSpeed = 0;
        agilitySpeed = 0;
        poisonChance = 0;
        bleedChance = 0;
        infectionChance = 0;
        brokenBoneChance = 0;
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

    private void updateRequiredKills() {
        if(level >= 95) {
            requiredKills = 100;
        } else if(level >= 90) {
            requiredKills = 85;
        } else if(level >= 80) {
            requiredKills = 70;
        } else if(level >= 70) {
            requiredKills = 60;
        } else if(level >= 60) {
            requiredKills = 50;
        } else if(level >= 50) {
            requiredKills = 45;
        } else if(level >= 40) {
            requiredKills = 35;
        } else if(level >= 30) {
            requiredKills = 30;
        } else if(level >= 20) {
            requiredKills = 25;
        } else if(level >= 10) {
            requiredKills = 15;
        } else requiredKills = 10;
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

    public void setBonemealCraftYield(int bonemealCraftYield) {
        this.bonemealCraftYield = bonemealCraftYield;
    }

    public int getBonemealCraftYield() {
        return bonemealCraftYield;
    }

    public void setPoisonResistance(int poisonResistance) {
        this.poisonResistance = Math.max(poisonResistance, this.poisonResistance);
    }

    public void setInfectionResistance(int infectionResistance) {
        this.infectionResistance = Math.max(infectionResistance, this.infectionResistance);
    }

    public void setBrokenBoneResistance(int brokenBoneResistance) {
        this.brokenBoneResistance = Math.max(brokenBoneResistance, this.brokenBoneResistance);
    }

    public void setBleedResistance(int bleedResistance) {
        this.bleedResistance = Math.max(bleedResistance, this.bleedResistance);
    }

    public void setExtraDamage(int extraDamage) {
        this.extraDamage = Math.max(extraDamage, this.extraDamage);
    }

    public void setInstantKillChance(float instantKillChance) {
        this.instantKillChance = Math.max(instantKillChance, this.instantKillChance);
    }

    public void setAxeMiningSpeed(float axeMiningSpeed) {
        this.axeMiningSpeed = Math.max(axeMiningSpeed, this.axeMiningSpeed);
    }

    public void setShovelMiningSpeed(float shovelMiningSpeed) {
        this.shovelMiningSpeed = Math.max(shovelMiningSpeed, this.shovelMiningSpeed);
    }

    public void setPickaxeMiningSpeed(float pickaxeMiningSpeed) {
        this.pickaxeMiningSpeed = Math.max(pickaxeMiningSpeed, this.pickaxeMiningSpeed);
    }

    public void setAcrobaticsFallResistance(float acrobaticsFallResistance) {
        this.acrobaticsFallResistance = Math.max(acrobaticsFallResistance, this.acrobaticsFallResistance);
    }

    public void setAcrobaticsExplosionResistance(float acrobaticsExplosionResistance) {
        this.acrobaticsExplosionResistance = Math.max(acrobaticsExplosionResistance, this.acrobaticsExplosionResistance);
    }

    public void setAgilitySpeed(float agilitySpeed) {
        this.agilitySpeed = Math.max(agilitySpeed, this.agilitySpeed);
    }

    public float getMovementSpeed() {
        return 0.1F + lightHunterMovementSpeed + agilitySpeed;
    }

    public void setPoisonChance(float poisonChance) {
        this.poisonChance = Math.max(this.poisonChance, poisonChance);
    }

    public void setBleedChance(float bleedChance) {
        this.bleedChance = Math.max(this.bleedChance, bleedChance);
    }

    public void setInfectionChance(float infectionChance) {
        this.infectionChance = Math.max(this.infectionChance, infectionChance);
    }

    public void setBrokenBoneChance(float brokenBoneChance) {
        this.brokenBoneChance = Math.max(this.brokenBoneChance, brokenBoneChance);
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
        nbt.setInteger("bonemealYield", bonemealCraftYield);
        nbt.setInteger("poisonResistance", poisonResistance);
        nbt.setInteger("infectionResistance", infectionResistance);
        nbt.setInteger("brokenBoneResistance", brokenBoneResistance);
        nbt.setInteger("bleedingResistance", bleedResistance);
        nbt.setInteger("extraDamage", extraDamage);
        nbt.setFloat("instantKill", instantKillChance);
        nbt.setFloat("axeSpeed", axeMiningSpeed);
        nbt.setFloat("shovelSpeed", shovelMiningSpeed);
        nbt.setFloat("pickaxeSpeed", pickaxeMiningSpeed);
        nbt.setFloat("acrobaticsFallResistance", acrobaticsFallResistance);
        nbt.setFloat("acrobaticsExplosionResistance", acrobaticsExplosionResistance);
        nbt.setFloat("lightMovementSpeed", lightHunterMovementSpeed);
        nbt.setFloat("agilitySpeed", agilitySpeed);
        nbt.setFloat("poisonChance", poisonChance);
        nbt.setFloat("bleedChance", bleedChance);
        nbt.setFloat("infectionChance", infectionChance);
        nbt.setFloat("brokenBoneChance", brokenBoneChance);
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
        bonemealCraftYield = nbt.getInteger("bonemealYield");
        poisonResistance = nbt.getInteger("poisonResistance");
        infectionResistance = nbt.getInteger("infectionResistance");
        brokenBoneResistance = nbt.getInteger("brokenBoneResistance");
        bleedResistance = nbt.getInteger("bleedingResistance");
        extraDamage = nbt.getInteger("extraDamage");
        instantKillChance = nbt.getFloat("instantKill");
        axeMiningSpeed = nbt.getFloat("axeSpeed");
        shovelMiningSpeed = nbt.getFloat("shovelSpeed");
        pickaxeMiningSpeed = nbt.getFloat("pickaxeSpeed");
        acrobaticsFallResistance = nbt.getFloat("acrobaticsFallResistance");
        acrobaticsExplosionResistance = nbt.getFloat("acrobaticsExplosionResistance");
        lightHunterMovementSpeed = nbt.getFloat("lightMovementSpeed");
        agilitySpeed = nbt.getFloat("agilitySpeed");
        poisonChance = nbt.getFloat("poisonChance");
        bleedChance = nbt.getFloat("bleedChance");
        infectionChance = nbt.getFloat("infectionChance");
        brokenBoneChance = nbt.getFloat("brokenBoneChance");
    }

    public float getTotalFallResistance() {
        EntityPlayer player = data.getPlayer();
        float f = hasSkill(ModRegistry.Skills.LIGHT_HUNTER) && getSkill(ModRegistry.Skills.LIGHT_HUNTER).apply(player) ? 0.1F : 0.0F;
        return f + acrobaticsFallResistance;
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
