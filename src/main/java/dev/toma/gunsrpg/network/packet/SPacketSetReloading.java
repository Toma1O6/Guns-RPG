package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.ReloadInfo;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSetReloading implements IMessage {

    boolean reloading;
    int time;

    public SPacketSetReloading() {

    }

    public SPacketSetReloading(boolean reload, int time) {
        this.reloading = reload;
        this.time = time;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(reloading);
        buf.writeInt(time);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        reloading = buf.readBoolean();
        time = buf.readInt();
    }

    public static class Handler implements IMessageHandler<SPacketSetReloading, IMessage> {

        @Override
        public IMessage onMessage(SPacketSetReloading message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                PlayerData data = PlayerDataFactory.get(player);
                ReloadInfo reloadInfo = data.getReloadInfo();
                if(message.reloading) {
                    reloadInfo.startReloading(player.inventory.currentItem, message.time);
                } else reloadInfo.cancelReload();
                data.sync();
            });
            return null;
        }
    }
}
