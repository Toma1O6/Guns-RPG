package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.LootStashDetectorHandler;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2C_UseStashDetectorPacket extends AbstractNetworkPacket<S2C_UseStashDetectorPacket> {

    private final boolean using;

    public S2C_UseStashDetectorPacket() {
        this(false);
    }

    public S2C_UseStashDetectorPacket(boolean using) {
        this.using = using;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(using);
    }

    @Override
    public S2C_UseStashDetectorPacket decode(PacketBuffer buffer) {
        return new S2C_UseStashDetectorPacket(buffer.readBoolean());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (using) {
            LootStashDetectorHandler.initiateUsing(player);
        } else {
            LootStashDetectorHandler.stopUsing(player.getUUID());
        }
    }
}
