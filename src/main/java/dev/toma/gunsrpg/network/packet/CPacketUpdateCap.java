package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public class CPacketUpdateCap extends AbstractNetworkPacket<CPacketUpdateCap> {

    private final UUID uuid;
    private final CompoundNBT nbt;

    public CPacketUpdateCap() {
        this(null, null);
    }

    public CPacketUpdateCap(UUID uuid, CompoundNBT nbt) {
        this.uuid = uuid;
        this.nbt = nbt;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
        buffer.writeNbt(nbt);
    }

    @Override
    public CPacketUpdateCap decode(PacketBuffer buffer) {
        return new CPacketUpdateCap(buffer.readUUID(), buffer.readNbt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player == null)
            return;
        PlayerData.get(player).ifPresent(data -> {
            data.deserializeNBT(nbt);
            data.onSync();
        });
    }
}
