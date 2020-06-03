package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.ScopeData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketUpdateSightData implements IMessage {

    public SPacketUpdateSightData() {}

    int type, color;

    public SPacketUpdateSightData(int type, int color) {
        this.type = type;
        this.color = color;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(type);
        buf.writeInt(color);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = buf.readInt();
        color = buf.readInt();
    }

    public static class Handler implements IMessageHandler<SPacketUpdateSightData, IMessage> {

        @Override
        public IMessage onMessage(SPacketUpdateSightData message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                ScopeData scopeData = PlayerDataFactory.get(player).getScopeData();
                scopeData.setNew(message.type, message.color);
            });
            return null;
        }
    }
}
