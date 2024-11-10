package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.ISyncRequestDispatcher;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IHandState;
import dev.toma.gunsrpg.api.common.data.IJamInfo;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

public class JamInfo implements IJamInfo, IPlayerCapEntry {

    private final IHandState handState;
    private final PlayerEntity player;
    private ISyncRequestDispatcher syncTemplate = () -> {};
    private boolean unjamming;
    private int activeSlot;
    private int remainingTime;

    public JamInfo(IHandState handState, PlayerEntity player) {
        this.handState = handState;
        this.player = player;
    }

    @Override
    public void startUnjamming(int slot, int time) {
        handState.setHandsBusy();
        this.activeSlot = slot;
        this.remainingTime = time;
        setUnjamming(true);
        syncTemplate.sendSyncRequest();
    }

    @Override
    public void tick() {
        if (unjamming) {
            if (--remainingTime <= 0) {
                completeUnjamming();
            } else if (player.inventory.selected != activeSlot) {
                interruptUnjam();
                if (player.level.isClientSide) {
                    interruptAnimation();
                }
            }
        }
    }

    @Override
    public boolean isUnjamming() {
        return unjamming;
    }

    @Override
    public void setUnjamming(boolean unjamming) {
        this.unjamming = unjamming;
    }

    @Override
    public int getFlag() {
        return DataFlags.JAMS;
    }

    @Override
    public void toNbt(CompoundNBT cnbt) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("unjamming", unjamming);
        nbt.putInt("slot", activeSlot);
        nbt.putInt("remainingTime", remainingTime);
        cnbt.put("jamInfo", nbt);
    }

    @Override
    public void fromNbt(CompoundNBT cnbt) {
        CompoundNBT nbt = cnbt.contains("jamInfo", Constants.NBT.TAG_COMPOUND) ? cnbt.getCompound("jamInfo") : new CompoundNBT();
        unjamming = nbt.getBoolean("unjamming");
        activeSlot = nbt.getInt("slot");
        remainingTime = nbt.getInt("remainingTime");
    }

    @Override
    public void setSyncRequestTemplate(ISyncRequestDispatcher request) {
        this.syncTemplate = request;
    }

    private void completeUnjamming() {
        interruptUnjam();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof AbstractGun) {
            AbstractGun gun = (AbstractGun) stack.getItem();
            gun.setJammedState(stack, false);
        }
    }

    private void interruptUnjam() {
        setUnjamming(false);
        handState.freeHands();
        syncTemplate.sendSyncRequest();
    }

    @OnlyIn(Dist.CLIENT)
    private void interruptAnimation() {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        pipeline.remove(ModAnimations.UNJAM);
    }
}
