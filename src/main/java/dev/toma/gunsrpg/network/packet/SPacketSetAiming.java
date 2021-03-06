package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.AimInfo;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSetAiming implements IMessage {

    public SPacketSetAiming() {}

    private boolean aim;

    public SPacketSetAiming(boolean aim) {
        this.aim = aim;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(aim);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        aim = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<SPacketSetAiming, IMessage> {

        @Override
        public IMessage onMessage(SPacketSetAiming message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                PlayerData data = PlayerDataFactory.get(player);
                AimInfo aimInfo = data.getAimInfo();
                aimInfo.setAiming(message.aim);
                data.sync();
            });
            return null;
        }
    }
}
