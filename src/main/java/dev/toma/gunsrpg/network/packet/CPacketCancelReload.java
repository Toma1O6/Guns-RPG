package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.util.object.ReloadTracker;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketCancelReload implements IMessage {

    private boolean forced;

    public CPacketCancelReload() {

    }

    public CPacketCancelReload(boolean forced) {
        this.forced = forced;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(forced);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        forced = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<CPacketCancelReload, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(CPacketCancelReload message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                EntityPlayer player = mc.player;
                ItemStack stack = player.getHeldItemMainhand();
                ReloadTracker.stopTracking(player);
                if(!message.forced) {
                    if(stack.getItem() instanceof GunItem) {
                        GunItem item = (GunItem) stack.getItem();
                        item.getReloadManager().finishReload(player, item, stack);
                    }
                }
            });
            return null;
        }
    }
}
