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

    private final Type type;
    private final BlockPos pos;

    public S2C_SetTrackedStashPacket() {
        this.pos = null;
        this.type = Type.UNSET;
    }

    public S2C_SetTrackedStashPacket(BlockPos pos) {
        this.pos = pos;
        this.type = Type.SET;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(type);
        if (type == Type.SET) {
            buffer.writeBlockPos(pos);
        }
    }

    @Override
    public S2C_SetTrackedStashPacket decode(PacketBuffer buffer) {
        Type type = buffer.readEnum(Type.class);
        if (type == Type.SET) {
            return new S2C_SetTrackedStashPacket(buffer.readBlockPos());
        } else {
            return new S2C_SetTrackedStashPacket();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        LootStashDetectorHandler.DetectionData data = LootStashDetectorHandler.getData(player.getUUID());
        if (type == Type.SET) {
            data.setTrackedLocation(pos);
        } else {
            data.setTrackedLocation(null);
        }
    }

    public enum Type {
        SET,
        UNSET
    }
}
