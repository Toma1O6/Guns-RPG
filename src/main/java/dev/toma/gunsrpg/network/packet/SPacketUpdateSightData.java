package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.ScopeData;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @deprecated Replace with config entry, packet won't be needed anymore
 */
@Deprecated
public class SPacketUpdateSightData extends AbstractNetworkPacket<SPacketUpdateSightData> {

    public SPacketUpdateSightData() {}

    int type, color;

    public SPacketUpdateSightData(int type, int color) {
        this.type = type;
        this.color = color;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeVarInt(type);
        buffer.writeVarInt(color);
    }

    @Override
    public SPacketUpdateSightData decode(PacketBuffer buffer) {
        return new SPacketUpdateSightData(buffer.readVarInt(), buffer.readVarInt());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        PlayerDataFactory.get(player).ifPresent(data -> {
            ScopeData scopes = data.getScopeData();
            scopes.setNew(type, color);
        });
    }
}
