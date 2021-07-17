package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketSendAnimation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;

public class ReloadInfo {

    private final PlayerDataFactory factory;
    private int slot;
    private boolean reloading;
    private int total, left;

    public ReloadInfo(PlayerDataFactory factory) {
        this.factory = factory;
    }

    public void update() {
        if (reloading) {
            int slot = factory.getPlayer().inventory.selected;
            boolean serverSide = !factory.getPlayer().level.isClientSide;
            if (serverSide && this.slot != slot) {
                cancelReload();
                factory.sync();
                return;
            }
            if (--left <= 0 && serverSide) {
                cancelReload();
                factory.sync();
                PlayerEntity player = factory.getPlayer();
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof GunItem) {
                    GunItem gunItem = (GunItem) stack.getItem();
                    gunItem.getReloadManager().finishReload(factory.getPlayer(), gunItem, stack);
                }
            }
        }
    }

    public void startReloading(int slot, int time) {
        if (!reloading) {
            PlayerEntity player = factory.getPlayer();
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof GunItem) {
                player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ((GunItem) stack.getItem()).getReloadSound(player), SoundCategory.MASTER, 1.0F, 1.0F);
                NetworkManager.sendClientPacket((ServerPlayerEntity) player, new CPacketSendAnimation(Animations.RELOAD));
            }
        }
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

    public CompoundNBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("reloading", reloading);
        nbt.putInt("slot", slot);
        nbt.putInt("total", total);
        nbt.putInt("left", left);
        return nbt;
    }

    public void read(CompoundNBT nbt) {
        reloading = nbt.getBoolean("reloading");
        slot = nbt.getInt("slot");
        total = nbt.getInt("total");
        left = nbt.getInt("left");
    }
}
