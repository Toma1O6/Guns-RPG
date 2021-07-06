package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.AimInfo;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SPacketSetAiming extends AbstractNetworkPacket<SPacketSetAiming> {

    public SPacketSetAiming() {}

    private boolean aim;

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
        PlayerDataFactory.get(player).ifPresent(data -> {
            AimInfo info = data.getAimInfo();
            info.setAiming(aim);
            data.sync();
        });
    }
}
