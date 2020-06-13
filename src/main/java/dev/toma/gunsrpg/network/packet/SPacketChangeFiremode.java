package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketChangeFiremode implements IMessage {

    public SPacketChangeFiremode() {}

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<SPacketChangeFiremode, IMessage> {

        @Override
        public IMessage onMessage(SPacketChangeFiremode message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                ItemStack stack = player.getHeldItemMainhand();
                if(stack.getItem() instanceof GunItem) {
                    GunItem item = (GunItem) stack.getItem();
                    if(item.switchFiremode(stack, player)) {
                        player.sendStatusMessage(new TextComponentString("Firemode updated to: " + item.getFiremode(stack).getName()), true);
                        NetworkManager.toClient(player, new CPacketSendAnimation(Animations.FIREMODE));
                    }
                }
            });
            return null;
        }
    }
}
