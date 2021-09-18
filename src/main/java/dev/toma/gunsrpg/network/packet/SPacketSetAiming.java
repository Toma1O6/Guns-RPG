package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IAimInfo;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SPacketSetAiming extends AbstractNetworkPacket<SPacketSetAiming> {

    private boolean aim;

    public SPacketSetAiming() {
    }

    public SPacketSetAiming(boolean aim) {
        this.aim = aim;
    }

    @Override
    public void encode(PacketBuffer buf) {
        buf.writeBoolean(aim);
    }

    @Override
    public SPacketSetAiming decode(PacketBuffer buf) {
        return new SPacketSetAiming(buf.readBoolean());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        PlayerData.get(player).ifPresent(data -> {
            IAimInfo info = data.getAimInfo();
            info.setAiming(aim, player);
            data.sync(DataFlags.AIM);
        });
    }
}
