package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.ReloadInfo;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
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
        PlayerDataFactory.get(player).ifPresent(data -> {
            ReloadInfo info = data.getReloadInfo();
            if (reloading)
                info.startReloading(player.inventory.selected, time);
            else
                info.cancelReload();
            data.sync();
        });
    }
}
