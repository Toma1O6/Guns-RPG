package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.util.object.ReloadTracker;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketStartReloading implements IMessage {

    public SPacketStartReloading() {}

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<SPacketStartReloading, IMessage> {

        @Override
        public IMessage onMessage(SPacketStartReloading message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                ItemStack stack = player.getHeldItemMainhand();
                if(stack.getItem() instanceof GunItem) {
                    GunItem gun = (GunItem) stack.getItem();
                    ReloadTracker.startReload(player, gun);
                }
            });
            return null;
        }
    }
}
