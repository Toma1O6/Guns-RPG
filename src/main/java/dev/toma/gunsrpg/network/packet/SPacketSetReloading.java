package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.api.common.data.IReloadInfo;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SPacketSetReloading extends AbstractNetworkPacket<SPacketSetReloading> {

    boolean reloading;
    int time;

    public SPacketSetReloading() {
    }

    public SPacketSetReloading(boolean reload, int time) {
        this.reloading = reload;
        this.time = time;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(reloading);
        buffer.writeInt(time);
    }

    @Override
    public SPacketSetReloading decode(PacketBuffer buffer) {
        return new SPacketSetReloading(buffer.readBoolean(), buffer.readInt());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        PlayerData.get(player).ifPresent(data -> {
            IReloadInfo info = data.getReloadInfo();
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof GunItem) {
                GunItem gun = (GunItem) stack.getItem();
                if (reloading) {
                    info.startReloading(player, gun, stack, player.inventory.selected);
                } else {
                    info.enqueueCancel();
                }
            }
        });
    }
}
