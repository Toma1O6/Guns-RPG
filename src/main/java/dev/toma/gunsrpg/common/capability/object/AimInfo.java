package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class AimInfo {

    private final PlayerDataFactory parent;
    public int slot;
    public boolean aiming;
    public float progress;

    public AimInfo(PlayerDataFactory parent) {
        this.parent = parent;
    }

    public void update() {
        EntityPlayer player = parent.getPlayer();
        boolean server = !player.world.isRemote;
        int slotIn = player.inventory.currentItem;
        if(server && (slotIn != slot || player.isSprinting() || parent.getReloadInfo().isReloading())) {
            setAiming(false);
            parent.sync();
        }
        float aimingSpeed = 0.25F;
        if(aiming && progress < 1.0F) {
            progress = Math.min(1.0F, progress + aimingSpeed);
        } else if(!aiming && progress > 0.0F) {
            progress = Math.max(0.0F, progress - aimingSpeed);
        }
    }

    public void setAiming(boolean aiming) {
        if(aiming) {
            slot = parent.getPlayer().inventory.currentItem;
        }
        this.aiming = aiming;
    }

    public boolean isAiming() {
        return progress == 1.0F;
    }

    public float getProgress() {
        return progress;
    }

    public NBTTagCompound write() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("slot", slot);
        nbt.setBoolean("aim", aiming);
        nbt.setFloat("progress", progress);
        return nbt;
    }

    public void read(NBTTagCompound nbt) {
        slot = nbt.getInteger("slot");
        aiming = nbt.getBoolean("aim");
        progress = nbt.getFloat("progress");
    }
}
