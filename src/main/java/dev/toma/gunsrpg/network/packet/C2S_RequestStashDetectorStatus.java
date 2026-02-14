package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.LootStashDetectorHandler;
import dev.toma.gunsrpg.common.item.StashDetectorItem;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.network.NetworkManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_RequestStashDetectorStatus extends AbstractNetworkPacket<C2S_RequestStashDetectorStatus> {

    private final StashDetectorItem.StatusEvent event;

    public C2S_RequestStashDetectorStatus(PacketBuffer buffer) {
        this(buffer.readEnum(StashDetectorItem.StatusEvent.class));
    }

    public C2S_RequestStashDetectorStatus(StashDetectorItem.StatusEvent event) {
        this.event = event;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(event);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        StashDetectorItem.StatusEvent statusEvent = event;
        if (statusEvent == StashDetectorItem.StatusEvent.TOGGLE) {
            statusEvent = LootStashDetectorHandler.isUsing(player.getUUID()) ? StashDetectorItem.StatusEvent.OFF : StashDetectorItem.StatusEvent.ON;
        }
        switch (statusEvent) {
            case ON:
                LootStashDetectorHandler.initiateUsing(player);
                break;
            case OFF:
                LootStashDetectorHandler.stopUsing(player.getUUID());
                break;
        }
        NetworkManager.sendClientPacket(player, new S2C_UseStashDetectorPacket(statusEvent));
    }
}
