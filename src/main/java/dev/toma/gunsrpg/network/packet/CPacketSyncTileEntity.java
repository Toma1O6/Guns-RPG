package dev.toma.gunsrpg.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketSyncTileEntity implements IMessage {

    private NBTTagCompound nbt;
    private BlockPos pos;

    public CPacketSyncTileEntity() {}

    public CPacketSyncTileEntity(TileEntity tileEntity) {
        this.nbt = tileEntity.writeToNBT(new NBTTagCompound());
        this.pos = tileEntity.getPos();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        ByteBufUtils.writeTag(buf, nbt);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        nbt = ByteBufUtils.readTag(buf);
    }

    public static class Handler implements IMessageHandler<CPacketSyncTileEntity, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(CPacketSyncTileEntity message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                World world = mc.world;
                BlockPos pos = message.pos;
                if(pos != null && world.isBlockLoaded(pos)) {
                    TileEntity tileEntity = world.getTileEntity(pos);
                    if(tileEntity == null) return;
                    tileEntity.readFromNBT(message.nbt);
                }
            });
            return null;
        }
    }
}
