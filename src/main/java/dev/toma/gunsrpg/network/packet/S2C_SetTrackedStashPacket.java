package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.LootStashDetectorHandler;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2C_SetTrackedStashPacket extends AbstractNetworkPacket<S2C_SetTrackedStashPacket> {

    private final BlockPos pos;

    public S2C_SetTrackedStashPacket() {
        this(null);
    }

    public S2C_SetTrackedStashPacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
    }

    @Override
    public S2C_SetTrackedStashPacket decode(PacketBuffer buffer) {
        return new S2C_SetTrackedStashPacket(buffer.readBlockPos());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        LootStashDetectorHandler.DetectionData data = LootStashDetectorHandler.getData(player.getUUID());
        data.setTrackedLocation(pos);
    }
}
