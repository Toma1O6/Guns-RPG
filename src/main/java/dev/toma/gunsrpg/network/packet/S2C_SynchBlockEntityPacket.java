package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.entity.ISynchronizable;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2C_SynchBlockEntityPacket extends AbstractNetworkPacket<S2C_SynchBlockEntityPacket> {

    private final BlockPos pos;

    public S2C_SynchBlockEntityPacket() {
        this(null);
    }

    public S2C_SynchBlockEntityPacket(BlockPos tilePos) {
        pos = tilePos;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
    }

    @Override
    public S2C_SynchBlockEntityPacket decode(PacketBuffer buffer) {
        return new S2C_SynchBlockEntityPacket(buffer.readBlockPos());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        ClientWorld clientLevel = mc.level;
        TileEntity tile = clientLevel.getBlockEntity(pos);
        if (tile instanceof ISynchronizable) {
            ((ISynchronizable) tile).onSynch();
        }
    }
}
