package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.api.common.data.IReloadInfo;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_SetReloadingPacket extends AbstractNetworkPacket<C2S_SetReloadingPacket> {

    boolean reloading;
    int time;

    public C2S_SetReloadingPacket() {
    }

    public C2S_SetReloadingPacket(boolean reload, int time) {
        this.reloading = reload;
        this.time = time;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(reloading);
        buffer.writeInt(time);
    }

    @Override
    public C2S_SetReloadingPacket decode(PacketBuffer buffer) {
        return new C2S_SetReloadingPacket(buffer.readBoolean(), buffer.readInt());
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
