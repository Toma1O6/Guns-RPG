package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.ISyncRequestDispatcher;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IAimInfo;
import dev.toma.gunsrpg.api.common.data.IHandState;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import lib.toma.animations.AnimationUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

public class AimInfo implements IAimInfo, IPlayerCapEntry {

    private final IHandState handState;
    private ISyncRequestDispatcher request = () -> {};
    private int slot;
    private boolean aiming;
    private float progress;
    private float progressOld;

    public AimInfo(IHandState state) {
        this.handState = state;
    }

    @Override
    public void tick(PlayerEntity player) {
        boolean server = !player.level.isClientSide;
        int slotIn = player.inventory.selected;
        if (server && aiming && (slotIn != slot || player.getMainHandItem().isEmpty() || player.isSprinting() || handState.areHandsBusy())) {
            setAiming(false, player);
            request.sendSyncRequest();
        }
        float aimingSpeed = 0.175F;
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
        return AnimationUtils.linearInterpolate(progress, progressOld, deltaTime);
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
    public void setSyncRequestTemplate(ISyncRequestDispatcher request) {
        this.request = request;
    }
}
