package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IAimInfo;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.data.IReloadInfo;
import lib.toma.animations.Interpolate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

public class AimInfo implements IAimInfo, IPlayerCapEntry {

    private IClientSynchReq request = () -> {};
    private int slot;
    private boolean aiming;
    private float progress;
    private float progressOld;

    @Override
    public void tick(PlayerEntity player, IReloadInfo reloadStats) {
        boolean server = !player.level.isClientSide;
        int slotIn = player.inventory.selected;
        if (server && aiming && (slotIn != slot || player.isSprinting() || reloadStats.isReloading())) {
            setAiming(false, player);
            request.makeSyncRequest();
        }
        float aimingSpeed = 0.25F;
        progressOld = progress;
        if (aiming && progress < 1.0F) {
            progress = Math.min(1.0F, progress + aimingSpeed);
        } else if (!aiming && progress > 0.0F) {
            progress = Math.max(0.0F, progress - aimingSpeed);
        }
    }

    @Override
    public int getFlag() {
        return DataFlags.AIM;
    }

    @Override
    public boolean isAiming() {
        return progress == 1.0F;
    }

    @Override
    public boolean startedAiming() {
        return aiming;
    }

    @Override
    public void setAiming(boolean aiming, PlayerEntity player) {
        if (aiming) {
            slot = player.inventory.selected;
        }
        this.aiming = aiming;
    }

    @Override
    public float getProgress() {
        return progress;
    }

    @Override
    public float getProgress(float deltaTime) {
        return Interpolate.linear(deltaTime, progress, progressOld);
    }

    @Override
    public void toNbt(CompoundNBT nbt) {
        CompoundNBT cnbt = new CompoundNBT();
        cnbt.putInt("slot", slot);
        cnbt.putBoolean("aim", aiming);
        cnbt.putFloat("progress", progress);
        nbt.put("aimInfo", cnbt);
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        CompoundNBT cnbt = nbt.contains("aimInfo", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("aimInfo") : new CompoundNBT();
        slot = cnbt.getInt("slot");
        aiming = cnbt.getBoolean("aim");
        progress = cnbt.getFloat("progress");
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        this.request = request;
    }
}
