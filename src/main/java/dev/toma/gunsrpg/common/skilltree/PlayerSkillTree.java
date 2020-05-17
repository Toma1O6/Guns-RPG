package dev.toma.gunsrpg.common.skilltree;

import dev.toma.gunsrpg.common.capability.object.SkillData;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerSkillTree {

    private final EntityPlayer player;
    private final SkillData data;
    private List<EntryInstance> obtained;
    private List<EntryInstance> locked;

    public PlayerSkillTree(EntityPlayer player, SkillData data) {
        this.player = player;
        this.data = data;
        this.obtained = ModUtils.newList(SkillTreeEntry.List.root.makeInstance());
        this.locked = ModUtils.newList(ModUtils.convert(SkillTreeEntry.List.root.child, SkillTreeEntry::makeInstance));
    }

    public void updateEntries() {
        Iterator<EntryInstance> iterator = locked.iterator();
        List<EntryInstance> unlocked = new ArrayList<>();
        while (iterator.hasNext()) {
            EntryInstance instance = iterator.next();
            if(instance.canUnlock(data.killCount)) {
                obtained.add(instance);
                unlocked.add(instance);
                data.killCount.put(instance.getType().gun, 0);
                if(player instanceof EntityPlayerMP) {
                    player.sendStatusMessage(new TextComponentString(TextFormatting.GREEN + "You have reached new weapon level!"), true);
                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, player.posX, player.posY, player.posZ, 0.75F, 1.0F));
                }
                iterator.remove();
            }
        }
        if(!unlocked.isEmpty()) {
            for(EntryInstance instance : unlocked) {
                SkillTreeEntry entry = instance.getType();
                SkillTreeEntry[] childs = entry.child;
                for(SkillTreeEntry child : childs) {
                    locked.add(child.makeInstance());
                }
            }
            updateEntries();
        }
    }

    public NBTTagCompound write() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList unlocked = new NBTTagList();
        if(obtained.isEmpty()) {
            SkillTreeEntry entry = SkillTreeEntry.List.root;
            obtained.add(entry.makeInstance());
            locked.clear();
            for(SkillTreeEntry e : entry.child) {
                locked.add(e.makeInstance());
            }
        }
        for(EntryInstance instance : obtained) {
            unlocked.appendTag(new NBTTagString(instance.getType().registryName.toString()));
        }
        NBTTagList disabled = new NBTTagList();
        for(EntryInstance instance : locked) {
            disabled.appendTag(new NBTTagString(instance.getType().registryName.toString()));
        }
        nbt.setTag("unlocked", unlocked);
        nbt.setTag("locked", disabled);
        return nbt;
    }

    public void read(NBTTagCompound nbt) {
        obtained.clear();
        locked.clear();
        NBTTagList unl = nbt.hasKey("unlocked") ? nbt.getTagList("unlocked", Constants.NBT.TAG_STRING) : new NBTTagList();
        NBTTagList loc = nbt.hasKey("locked") ? nbt.getTagList("locked", Constants.NBT.TAG_STRING) : new NBTTagList();
        for(int i = 0; i < unl.tagCount(); i++) {
            ResourceLocation k = new ResourceLocation(unl.getStringTagAt(i));
            SkillTreeEntry v = SkillTreeEntry.List.SKILLS.get(k);
            if(v == null) {
                continue;
            }
            obtained.add(v.makeInstance());
        }
        for(int i = 0; i < loc.tagCount(); i++) {
            ResourceLocation k = new ResourceLocation(loc.getStringTagAt(i));
            SkillTreeEntry v = SkillTreeEntry.List.SKILLS.get(k);
            if(v == null) {
                continue;
            }
            locked.add(v.makeInstance());
        }
    }

    public List<EntryInstance> getObtained() {
        return obtained;
    }

    public List<EntryInstance> getLocked() {
        return locked;
    }
}
