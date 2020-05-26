package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skilltree.PlayerSkillTree;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class SkillData {

    private final EntityPlayer player;
    private final PlayerSkillTree skillTree;
    private final AbilityData abilityData;
    public final Map<GunItem, KillData> killCount = new HashMap<>();

    public SkillData(EntityPlayer player) {
        this.player = player;
        this.skillTree = new PlayerSkillTree(player, this);
        this.abilityData = new AbilityData(this);
    }

    public void kill(Entity entity, GunItem item) {
        KillData data = killCount.computeIfAbsent(item, gun -> new KillData());
        data.kill();
        skillTree.updateEntries(false);
        PlayerDataFactory.get(player).sync();
    }

    public PlayerSkillTree getSkillTree() {
        return skillTree;
    }

    public AbilityData getAbilityData() {
        return abilityData;
    }

    public NBTTagCompound write() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("tree", skillTree.write());
        NBTTagCompound map = new NBTTagCompound();
        for(Map.Entry<GunItem, KillData> entry : killCount.entrySet()) {
            map.setTag(entry.getKey().getRegistryName().toString(), entry.getValue().write());
        }
        nbt.setTag("map", map);
        nbt.setTag("abilities", abilityData.write());
        return nbt;
    }

    public void read(NBTTagCompound nbt) {
        killCount.clear();
        skillTree.read(nbt.hasKey("tree") ? nbt.getCompoundTag("tree") : new NBTTagCompound());
        NBTTagCompound map = nbt.hasKey("map") ? nbt.getCompoundTag("map") : new NBTTagCompound();
        for (String key : map.getKeySet()) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
            if (!(item instanceof GunItem)) {
                continue;
            }
            KillData data = new KillData();
            data.read(map.hasKey(key) ? map.getCompoundTag(key) : new NBTTagCompound());
            killCount.put((GunItem) item, data);
        }
        abilityData.read(nbt.hasKey("abilities") ? nbt.getCompoundTag("abilities") : new NBTTagCompound());
    }

    public static class KillData {
        private int kills;
        private int skills;

        public static KillData empty() {
            return new KillData();
        }

        public void kill() {
            this.kills++;
        }

        public void clearKillCount() {
            this.kills = 0;
        }

        public void addPoints(int n) {
            this.skills += n;
        }

        public void removePoints(int n) {
            this.skills -= n;
        }

        public int getKillCount() {
            return kills;
        }

        public int getSkillPoints() {
            return skills;
        }

        public NBTTagCompound write() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("kills", kills);
            nbt.setInteger("skillPoints", skills);
            return nbt;
        }

        public void read(NBTTagCompound nbt) {
            kills = nbt.getInteger("kills");
            skills = nbt.getInteger("skillPoints");
        }
    }
}
