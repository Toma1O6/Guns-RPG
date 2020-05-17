package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.network.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class SPacketRequestDataUpdate implements IMessage {

    private UUID uuid;

    public SPacketRequestDataUpdate() {
    }

    public SPacketRequestDataUpdate(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, uuid.toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    public static class Handler implements IMessageHandler<SPacketRequestDataUpdate, IMessage> {

        @Override
        public IMessage onMessage(SPacketRequestDataUpdate message, MessageContext ctx) {
            EntityPlayerMP playerMP = ctx.getServerHandler().player;
            playerMP.getServer().addScheduledTask(() -> {
                NBTTagCompound nbt = PlayerDataFactory.get(playerMP).serializeNBT();
                NetworkManager.toClient(playerMP, new CPacketUpdateCap(message.uuid, nbt, 0));
            });
            return null;
        }
    }
}
