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

public class S2C_UpdateCapabilityPacket extends AbstractNetworkPacket<S2C_UpdateCapabilityPacket> {

    private final UUID uuid;
    private final CompoundNBT nbt;
    private final int flags;

    public S2C_UpdateCapabilityPacket() {
        this(null, null, -1);
    }

    public S2C_UpdateCapabilityPacket(UUID uuid, CompoundNBT nbt, int flags) {
        this.uuid = uuid;
        this.nbt = nbt;
        this.flags = flags;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
        buffer.writeNbt(nbt);
        buffer.writeInt(flags);
    }

    @Override
    public S2C_UpdateCapabilityPacket decode(PacketBuffer buffer) {
        return new S2C_UpdateCapabilityPacket(buffer.readUUID(), buffer.readNbt(), buffer.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.level.getPlayerByUUID(this.uuid);
        if (player == null)
            return;
        PlayerData.get(player).ifPresent(data -> {
            data.fromNbt(nbt, flags);
            data.onSync();
        });
    }
}
