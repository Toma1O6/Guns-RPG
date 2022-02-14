package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IHandState;
import dev.toma.gunsrpg.api.common.data.IJamInfo;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.network.NetworkManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_PacketSetJamming extends AbstractNetworkPacket<C2S_PacketSetJamming> {

    private final boolean state;
    private final int time;
    private final int slot;

    public C2S_PacketSetJamming() {
        this(false, 0, 0);
    }

    public C2S_PacketSetJamming(int time, int slot) {
        this(true, time, slot);
    }

    public C2S_PacketSetJamming(boolean state, int time, int slot) {
        this.state = state;
        this.time = time;
        this.slot = slot;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(state);
        if (state) {
            buffer.writeInt(time);
            buffer.writeInt(slot);
        }
    }

    @Override
    public C2S_PacketSetJamming decode(PacketBuffer buffer) {
        boolean state = buffer.readBoolean();
        return state ? new C2S_PacketSetJamming(buffer.readInt(), buffer.readInt()) : new C2S_PacketSetJamming();
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        PlayerData.get(player).ifPresent(data -> {
            IJamInfo jamInfo = data.getJamInfo();
            IHandState handState = data.getHandState();
            if (state) {
                if (handState.areHandsBusy()) {
                    GunsRPG.log.fatal(NetworkManager.MARKER, "Attempted to activate new hand blocking event with already busy hands");
                    return;
                }
                jamInfo.startUnjamming(slot, time);
            } else {
                jamInfo.setUnjamming(false);
            }
        });
    }
}
