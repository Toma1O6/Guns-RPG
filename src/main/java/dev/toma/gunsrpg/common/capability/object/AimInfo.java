package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.IAimInfo;
import dev.toma.gunsrpg.common.capability.PlayerData;
import lib.toma.animations.Interpolate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class AimInfo implements IAimInfo {

    private final PlayerData parent;
    public int slot;
    public boolean aiming;
    public float progress;
    public float progressOld;

    public AimInfo(PlayerData parent) {
        this.parent = parent;
    }

    public void update() {
        PlayerEntity player = parent.getPlayer();
        boolean server = !player.level.isClientSide;
        int slotIn = player.inventory.selected;
        if (server && aiming && (slotIn != slot || player.isSprinting() || parent.getReloadInfo().isReloading())) {
            setAiming(false);
            parent.sync();
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
    public boolean isAiming() {
        return progress == 1.0F;
    }

    @Override
    public boolean startedAiming() {
        return aiming;
    }

    @Override
    public void setAiming(boolean aiming) {
        if (aiming) {
            slot = parent.getPlayer().inventory.selected;
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

    public CompoundNBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("slot", slot);
        nbt.putBoolean("aim", aiming);
        nbt.putFloat("progress", progress);
        return nbt;
    }

    public void read(CompoundNBT nbt) {
        slot = nbt.getInt("slot");
        aiming = nbt.getBoolean("aim");
        progress = nbt.getFloat("progress");
    }
}
