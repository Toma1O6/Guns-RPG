package dev.toma.gunsrpg.util.object;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketCancelReload;
import dev.toma.gunsrpg.network.packet.SPacketStartReloading;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReloadTracker {

    private static final Map<UUID, TrackData> reloadData = new HashMap<>();

    public static void update(EntityPlayer player) {
        UUID uuid = player.getUniqueID();
        TrackData data = reloadData.get(uuid);
        if(data != null) {
            if(player.inventory.currentItem != data.slotIn) {
                reloadData.remove(uuid);
                NetworkManager.toClient((EntityPlayerMP) player, new CPacketCancelReload(true));
            } else if(data.timeLeft-- <= 0) {
                reloadData.remove(uuid);
                ItemStack stack = player.getHeldItemMainhand();
                if(stack.getItem() instanceof GunItem) {
                    GunItem item = (GunItem) stack.getItem();
                    item.getReloadManager().finishReload(player, item, stack);
                    NetworkManager.toClient((EntityPlayerMP) player, new CPacketCancelReload(false));
                } else NetworkManager.toClient((EntityPlayerMP) player, new CPacketCancelReload(true));
            }
        }
    }

    public static void stopTracking(EntityPlayer player) {
        reloadData.remove(player.getUniqueID());
    }

    public static boolean isReloading(EntityPlayer player) {
        TrackData data = reloadData.get(player.getUniqueID());
        return data != null && data.timeLeft > 0;
    }

    public static void startReload(EntityPlayer player, GunItem item) {
        reloadData.put(player.getUniqueID(), new TrackData(player.inventory.currentItem, item.getReloadTime(player)));
        if(player.world.isRemote) {
            NetworkManager.toServer(new SPacketStartReloading());
        }
    }

    private static class TrackData {

        private final int slotIn;
        private int timeLeft;

        TrackData(int slotIn, int timeLeft) {
            this.slotIn = slotIn;
            this.timeLeft = timeLeft;
        }
    }
}
