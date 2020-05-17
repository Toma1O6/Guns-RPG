package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.SkillData;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketUpdateCap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataFactory implements PlayerData {

    private final EntityPlayer player;
    private final DebuffData debuffData;
    private final SkillData skillData;

    private boolean logged = false;

    public PlayerDataFactory() {
        this(null);
    }

    public PlayerDataFactory(EntityPlayer player) {
        this.player = player;
        this.debuffData = new DebuffData();
        this.skillData = new SkillData(player);
    }

    public static PlayerData get(EntityPlayer player) {
        return player.getCapability(PlayerDataManager.CAPABILITY, null);
    }

    @Override
    public void tick() {
        this.debuffData.onTick(player);
    }

    @Override
    public DebuffData getDebuffData() {
        return debuffData;
    }

    @Override
    public SkillData getSkillData() {
        return skillData;
    }

    @Override
    public void sync() {
        if(player instanceof EntityPlayerMP) {
            NetworkManager.toClient((EntityPlayerMP) player, new CPacketUpdateCap(player.getUniqueID(), this.serializeNBT(), 0));
        }
    }

    @Override
    public void syncCloneData() {
        if(player instanceof EntityPlayerMP) {
            NetworkManager.toClient((EntityPlayerMP) player, new CPacketUpdateCap(player.getUniqueID(), this.writePermanentData(), 1));
        }
    }

    @Override
    public void handleLogin() {
        if(!logged) {
            logged = true;
            player.addItemStackToInventory(new ItemStack(ModRegistry.GRPGItems.ANTIDOTUM_PILLS, 2));
            player.addItemStackToInventory(new ItemStack(ModRegistry.GRPGItems.BANDAGE, 2));
            player.addItemStackToInventory(new ItemStack(ModRegistry.GRPGItems.VACCINE));
            player.addItemStackToInventory(new ItemStack(ModRegistry.GRPGItems.PLASTER_CAST));
            sync();
        }
    }

    public NBTTagCompound writePermanentData() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setBoolean("logged", logged);
        return nbtTagCompound;
    }

    public void readPermanentData(NBTTagCompound nbt) {
        logged = nbt.getBoolean("logged");
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("permanent", writePermanentData());
        nbt.setTag("debuffs", debuffData.serializeNBT());
        nbt.setTag("skills", skillData.write());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("permanent")) readPermanentData(nbt.getCompoundTag("permanent"));
        debuffData.deserializeNBT(nbt.hasKey("debuffs") ? nbt.getCompoundTag("debuffs") : new NBTTagCompound());
        skillData.read(nbt.hasKey("skills") ? nbt.getCompoundTag("skills") : new NBTTagCompound());
    }
}
