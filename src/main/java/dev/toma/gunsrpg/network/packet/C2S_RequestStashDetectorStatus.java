package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.client.animation.StashDetectorEvent;
import dev.toma.gunsrpg.common.LootStashDetectorHandler;
import dev.toma.gunsrpg.common.item.StashDetectorItem;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.world.LootStashes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_RequestStashDetectorStatus extends AbstractNetworkPacket<C2S_RequestStashDetectorStatus> {

    private final StashDetectorItem.StatusEvent event;

    public C2S_RequestStashDetectorStatus() {
        this(null);
    }

    public C2S_RequestStashDetectorStatus(StashDetectorItem.StatusEvent event) {
        this.event = event;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(event);
    }

    @Override
    public C2S_RequestStashDetectorStatus decode(PacketBuffer buffer) {
        return new C2S_RequestStashDetectorStatus(buffer.readEnum(StashDetectorItem.StatusEvent.class));
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
