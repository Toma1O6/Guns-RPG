package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.tileentity.CrystalPurificationStationTileEntity;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_PurifyPacket extends AbstractNetworkPacket<C2S_PurifyPacket> {

    final BlockPos pos;

    public C2S_PurifyPacket() {
        this(null);
    }

    public C2S_PurifyPacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
    }

    @Override
    public C2S_PurifyPacket decode(PacketBuffer buffer) {
        return new C2S_PurifyPacket(buffer.readBlockPos());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ServerWorld world = player.getLevel();
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof CrystalPurificationStationTileEntity) {
            CrystalPurificationStationTileEntity tile = (CrystalPurificationStationTileEntity) tileEntity;
            PlayerData.get(player).ifPresent(data -> {
                IPerkProvider provider = data.getPerkProvider();
                if (tile.canPurify(provider)) {
                    tile.purify(player);
                }
            });
        }
    }
}
