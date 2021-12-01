package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IAimInfo;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_SetAimingPacket extends AbstractNetworkPacket<C2S_SetAimingPacket> {

    private boolean aim;

    public C2S_SetAimingPacket() {
    }

    public C2S_SetAimingPacket(boolean aim) {
        this.aim = aim;
    }

    @Override
    public void encode(PacketBuffer buf) {
        buf.writeBoolean(aim);
    }

    @Override
    public C2S_SetAimingPacket decode(PacketBuffer buf) {
        return new C2S_SetAimingPacket(buf.readBoolean());
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
