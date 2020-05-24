package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.util.object.AimTracker;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketSetAiming implements IMessage {

    private boolean aim;

    public CPacketSetAiming() {

    }

    public CPacketSetAiming(boolean aim) {
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

    public static class Handler implements IMessageHandler<CPacketSetAiming, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(CPacketSetAiming message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                AimTracker.setAiming(mc.player, message.aim);
            });
            return null;
        }
    }
}
