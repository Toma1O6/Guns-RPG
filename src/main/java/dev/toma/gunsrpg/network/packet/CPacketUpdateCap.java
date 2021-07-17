package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public class CPacketUpdateCap extends AbstractNetworkPacket<CPacketUpdateCap> {

    private final UUID uuid;
    private final CompoundNBT nbt;
    private final int type;

    public CPacketUpdateCap() {
        this(null, null, 0);
    }

    public CPacketUpdateCap(UUID uuid, CompoundNBT nbt, int type) {
        this.uuid = uuid;
        this.nbt = nbt;
        this.type = type;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
        buffer.writeNbt(nbt);
        buffer.writeVarInt(type);
    }

    @Override
    public CPacketUpdateCap decode(PacketBuffer buffer) {
        return new CPacketUpdateCap(buffer.readUUID(), buffer.readNbt(), buffer.readVarInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player == null)
            return;
        PlayerDataFactory.get(player).ifPresent(data -> {
            switch (type) {
                case 0:
                    data.deserializeNBT(nbt);
                    break;
                case 1:
                    data.readPermanentData(nbt);
                    break;
            }
            data.onSync();
        });
    }
}
