package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSetShooting implements IMessage {

    public SPacketSetShooting() {}

    boolean shooting;

    public SPacketSetShooting(boolean shooting) {
        this.shooting = shooting;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(shooting);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        shooting = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<SPacketSetShooting, IMessage> {

        @Override
        public IMessage onMessage(SPacketSetShooting message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> PlayerDataFactory.get(player).setShooting(message.shooting));
            return null;
        }
    }
}
