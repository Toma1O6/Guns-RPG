package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketShoot implements IMessage {

    private GunItem gunItem;

    public SPacketShoot() {

    }

    public SPacketShoot(GunItem item) {
        this.gunItem = item;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(Item.getIdFromItem(gunItem));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        gunItem = (GunItem) Item.getItemById(buf.readInt());
    }

    public static class Handler implements IMessageHandler<SPacketShoot, IMessage> {
        @Override
        public IMessage onMessage(SPacketShoot message, MessageContext ctx) {
            EntityPlayerMP playerMP = ctx.getServerHandler().player;
            playerMP.getServer().addScheduledTask(() -> {
                ItemStack stack = playerMP.getHeldItemMainhand();
                Item item = stack.getItem();
                if(item instanceof GunItem) {

                }
            });
            return null;
        }
    }
}
