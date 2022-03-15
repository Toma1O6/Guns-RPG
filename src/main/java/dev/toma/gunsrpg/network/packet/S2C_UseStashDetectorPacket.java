package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.LootStashDetectorHandler;
import dev.toma.gunsrpg.common.item.StashDetectorItem;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2C_UseStashDetectorPacket extends AbstractNetworkPacket<S2C_UseStashDetectorPacket> {

    private final StashDetectorItem.StatusEvent event;

    public S2C_UseStashDetectorPacket() {
        this(null);
    }

    public S2C_UseStashDetectorPacket(StashDetectorItem.StatusEvent event) {
        this.event = event;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(event);
    }

    @Override
    public S2C_UseStashDetectorPacket decode(PacketBuffer buffer) {
        return new S2C_UseStashDetectorPacket(buffer.readEnum(StashDetectorItem.StatusEvent.class));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        switch (event) {
            case ON:
                LootStashDetectorHandler.initiateUsing(player);
                break;
            case OFF:
                LootStashDetectorHandler.stopUsing(player.getUUID());
                break;
        }
    }
}
