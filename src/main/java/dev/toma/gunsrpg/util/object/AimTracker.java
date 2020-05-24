package dev.toma.gunsrpg.util.object;

import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketSetAiming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AimTracker {

    private static final Map<UUID, AimData> data = new HashMap<>();

    public static void setAiming(EntityPlayer player, boolean aim) {
        AimData aimData = data.get(player.getUniqueID());
        if(aimData != null) {
            aimData.setAiming(aim);
        } else {
            data.put(player.getUniqueID(), new AimData(player.inventory.currentItem, aim));
        }
    }

    public static void toggleAim(EntityPlayer player) {
        AimData aimData = data.get(player.getUniqueID());
        if(aimData != null) {
            aimData.toggleAim();
        } else {
            data.put(player.getUniqueID(), new AimData(player.inventory.currentItem, true));
        }
    }

    public static boolean isAiming(EntityPlayer player) {
        AimData aimData = data.get(player.getUniqueID());
        return aimData != null && aimData.progress >= 1.0F;
    }

    public static void update(EntityPlayer player) {
        AimData aimData = data.get(player.getUniqueID());
        if(aimData != null) {
            int slot = player.inventory.currentItem;
            if(aimData.tick() || slot != aimData.trackedSlot || player.isSprinting()) {
                if(!player.world.isRemote) {
                    setAiming(player, false);
                    NetworkManager.toClient((EntityPlayerMP) player, new CPacketSetAiming(false));
                }
            }
        }
    }

    private static class AimData {

        private final int trackedSlot;
        private float progress;
        private boolean aiming;

        AimData(int slot, boolean aiming) {
            this.trackedSlot = slot;
            this.aiming = aiming;
        }

        void setAiming(boolean aiming) {
            this.aiming = aiming;
        }

        void toggleAim() {
            this.aiming = !this.aiming;
        }

        boolean tick() {
            float aimSpeed = 0.05F;
            if(aiming && progress < 1.0F) {
                progress += aimSpeed;
            } else if(!aiming && progress > 0.0F) {
                progress -= aimSpeed;
            }
            return progress <= 0;
        }
    }
}
