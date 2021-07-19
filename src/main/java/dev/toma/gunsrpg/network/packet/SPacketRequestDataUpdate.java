package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.network.NetworkManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public class SPacketRequestDataUpdate extends AbstractNetworkPacket<SPacketRequestDataUpdate> {

    private UUID uuid;

    public SPacketRequestDataUpdate() {
    }

    public SPacketRequestDataUpdate(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
    }

    @Override
    public SPacketRequestDataUpdate decode(PacketBuffer buffer) {
        return new SPacketRequestDataUpdate(buffer.readUUID());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        PlayerData.get(player).ifPresent(playerData -> {
            CompoundNBT nbt = playerData.serializeNBT();
            NetworkManager.sendClientPacket(player, new CPacketUpdateCap(uuid, nbt, 0));
        });
    }
}
