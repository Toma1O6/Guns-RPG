package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.common.capability.object.GunStats;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataFactory implements PlayerData {

    private EntityPlayer player;

    private GunStats pistolStats = new GunStats(null);

    public PlayerDataFactory() {
        this(null);
    }

    public PlayerDataFactory(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public GunStats getStats(GunItem item) {
        if(item == null) {
            return pistolStats;
        }
        return null;
    }

    @Override
    public void sync() {

    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("pistolStats", pistolStats.save());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        pistolStats.load(nbt.getCompoundTag("pistolStats"));
    }
}
