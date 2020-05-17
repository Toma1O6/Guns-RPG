package dev.toma.gunsrpg.common.capability.object;

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

    private final EntityPlayer owner;
    private final PlayerSkillTree skillTree;
    public final Map<GunItem, Integer> killCount = new HashMap<>();

    public SkillData(EntityPlayer player) {
        this.owner = player;
        this.skillTree = new PlayerSkillTree(player, this);
    }

    public void kill(Entity entity, GunItem item) {
        int value = killCount.computeIfAbsent(item, gun -> 0);
        killCount.put(item, value + 1);
        skillTree.updateEntries();
    }

    public PlayerSkillTree getSkillTree() {
        return skillTree;
    }

    public NBTTagCompound write() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("tree", skillTree.write());
        NBTTagCompound map = new NBTTagCompound();
        for(Map.Entry<GunItem, Integer> entry : killCount.entrySet()) {
            map.setInteger(entry.getKey().getRegistryName().toString(), entry.getValue());
        }
        nbt.setTag("map", map);
        return nbt;
    }

    public void read(NBTTagCompound nbt) {
        killCount.clear();
        skillTree.read(nbt.hasKey("tree") ? nbt.getCompoundTag("tree") : new NBTTagCompound());
        NBTTagCompound map = nbt.hasKey("map") ? nbt.getCompoundTag("map") : new NBTTagCompound();
        for(String key : map.getKeySet()) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
            if(!(item instanceof GunItem)) {
                continue;
            }
            killCount.put((GunItem) item, map.getInteger(key));
        }
    }
}
