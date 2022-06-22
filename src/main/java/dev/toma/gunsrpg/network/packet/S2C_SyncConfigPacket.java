package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.config.world.WorldConfiguration;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2C_SyncConfigPacket extends AbstractNetworkPacket<S2C_SyncConfigPacket> {

    private final int bloodmoonDelay;

    public S2C_SyncConfigPacket() {
        this(0);
    }

    public S2C_SyncConfigPacket(WorldConfiguration worldConfiguration) {
        this(worldConfiguration.bloodmoonCycle.get());
    }

    private S2C_SyncConfigPacket(int bloodmoonDelay) {
        this.bloodmoonDelay = bloodmoonDelay;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(bloodmoonDelay);
    }

    @Override
    public S2C_SyncConfigPacket decode(PacketBuffer buffer) {
        return new S2C_SyncConfigPacket(buffer.readInt());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ModConfig.worldConfig.bloodmoonCycle.set(bloodmoonDelay);
    }
}
