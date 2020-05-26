package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ReloadInfo {

    private final PlayerDataFactory factory;
    private int slot;
    private boolean reloading;
    private int total, left;

    public ReloadInfo(PlayerDataFactory factory) {
        this.factory = factory;
    }

    public void update() {
        if(reloading) {
            int slot = factory.getPlayer().inventory.currentItem;
            boolean serverSide = !factory.getPlayer().world.isRemote;
            if(serverSide && this.slot != slot) {
                cancelReload();
                factory.sync();
                return;
            }
            if(--left <= 0 && serverSide) {
                cancelReload();
                factory.sync();
                EntityPlayer player = factory.getPlayer();
                ItemStack stack = player.getHeldItemMainhand();
                if(stack.getItem() instanceof GunItem) {
                    GunItem gunItem = (GunItem) stack.getItem();
                    gunItem.getReloadManager().finishReload(factory.getPlayer(), gunItem, stack);
                }
            }
        }
    }

    public void startReloading(int slot, int time) {
        this.reloading = true;
        this.slot = slot;
        this.total = time;
        this.left = time;
    }

    public void cancelReload() {
        this.reloading = false;
        this.total = 0;
        this.slot = 0;
        this.left = 0;
    }

    public int getTotal() {
        return total;
    }

    public int getLeft() {
        return left;
    }

    public boolean isReloading() {
        return reloading;
    }

    public float getProgress() {
        return left / (float) total;
    }

    public NBTTagCompound write() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("reloading", reloading);
        nbt.setInteger("slot", slot);
        nbt.setInteger("total", total);
        nbt.setInteger("left", left);
        return nbt;
    }

    public void read(NBTTagCompound nbt) {
        reloading = nbt.getBoolean("reloading");
        slot = nbt.getInteger("slot");
        total = nbt.getInteger("total");
        left = nbt.getInteger("left");
    }
}
