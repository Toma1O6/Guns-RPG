package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.GunStats;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketUpdateCap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataFactory implements PlayerData {

    private EntityPlayer player;

    private DebuffData debuffData = new DebuffData();
    private GunStats pistolStats = new GunStats(null);

    public PlayerDataFactory() {
        this(null);
    }

    public PlayerDataFactory(EntityPlayer player) {
        this.player = player;
    }

    public static PlayerData get(EntityPlayer player) {
        return player.getCapability(PlayerDataManager.CAPABILITY, null);
    }

    @Override
    public void tick() {
        this.debuffData.onTick(player);
    }

    @Override
    public GunStats getStats(GunItem item) {
        if(item == null) {
            return pistolStats;
        }
        return null;
    }

    @Override
    public DebuffData getDebuffData() {
        return debuffData;
    }

    @Override
    public void sync() {
        if(player instanceof EntityPlayerMP) {
            NetworkManager.toClient((EntityPlayerMP) player, new CPacketUpdateCap(player.getUniqueID(), this.serializeNBT()));
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("debuffs", debuffData.serializeNBT());
        nbt.setTag("pistolStats", pistolStats.save());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        debuffData.deserializeNBT(nbt.hasKey("debuffs") ? nbt.getCompoundTag("debuffs") : new NBTTagCompound());
        pistolStats.load(nbt.getCompoundTag("pistolStats"));
    }
}
