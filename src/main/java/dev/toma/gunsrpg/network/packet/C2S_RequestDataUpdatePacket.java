package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.network.NetworkManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public class C2S_RequestDataUpdatePacket extends AbstractNetworkPacket<C2S_RequestDataUpdatePacket> {

    private UUID uuid;

    public C2S_RequestDataUpdatePacket() {
    }

    public C2S_RequestDataUpdatePacket(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
    }

    @Override
    public C2S_RequestDataUpdatePacket decode(PacketBuffer buffer) {
        return new C2S_RequestDataUpdatePacket(buffer.readUUID());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        PlayerData.get(player).ifPresent(playerData -> {
            CompoundNBT nbt = playerData.toNbt(DataFlags.WILDCARD);
            NetworkManager.sendClientPacket(player, new S2C_UpdateCapabilityPacket(uuid, nbt, DataFlags.WILDCARD));
        });
    }
}
