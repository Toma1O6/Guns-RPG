package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.tileentity.RepairStationTileEntity;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_RequestRepairPacket extends AbstractNetworkPacket<C2S_RequestRepairPacket> {

    private final BlockPos pos;

    public C2S_RequestRepairPacket() {
        this(null);
    }

    public C2S_RequestRepairPacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
    }

    @Override
    public C2S_RequestRepairPacket decode(PacketBuffer buffer) {
        return new C2S_RequestRepairPacket(buffer.readBlockPos());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ServerWorld world = player.getLevel();
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof RepairStationTileEntity) {
            RepairStationTileEntity repairStation = (RepairStationTileEntity) tile;
            if (repairStation.canRepair()) {
                repairStation.repair(player);
            }
        }
    }
}
