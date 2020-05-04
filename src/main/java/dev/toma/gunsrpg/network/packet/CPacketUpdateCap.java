package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class CPacketUpdateCap implements IMessage {

    private UUID uuid;
    private NBTTagCompound nbt;

    public CPacketUpdateCap() {}

    public CPacketUpdateCap(UUID uuid, NBTTagCompound nbt) {
        this.uuid = uuid;
        this.nbt = nbt;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, uuid.toString());
        ByteBufUtils.writeTag(buf, nbt);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.nbt = ByteBufUtils.readTag(buf);
    }

    public static class Handler implements IMessageHandler<CPacketUpdateCap, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(CPacketUpdateCap message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                WorldClient client = Minecraft.getMinecraft().world;
                EntityPlayer player = client.getPlayerEntityByUUID(message.uuid);
                if(player == null) {
                    return;
                }
                PlayerData data = PlayerDataFactory.get(player);
                if(data == null) return;
                data.deserializeNBT(message.nbt);
            });
            return null;
        }
    }
}
