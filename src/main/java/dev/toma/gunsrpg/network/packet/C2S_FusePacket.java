package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.tileentity.CrystalFusionStationTileEntity;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_FusePacket extends AbstractNetworkPacket<C2S_FusePacket> {

    private final BlockPos pos;

    public C2S_FusePacket() {
        this.pos = null;
    }

    public C2S_FusePacket(CrystalFusionStationTileEntity tile) {
        this.pos = tile.getBlockPos();
    }

    private C2S_FusePacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
    }

    @Override
    public C2S_FusePacket decode(PacketBuffer buffer) {
        return new C2S_FusePacket(buffer.readBlockPos());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        World world = player.getLevel();
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof CrystalFusionStationTileEntity) {
            CrystalFusionStationTileEntity crystalFusionStationTile = (CrystalFusionStationTileEntity) tile;
            crystalFusionStationTile.fuse(player);
        }
    }
}
