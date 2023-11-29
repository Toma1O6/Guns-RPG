package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.tileentity.AmmoBenchTileEntity;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_AmmoBenchEventPacket extends AbstractNetworkPacket<C2S_AmmoBenchEventPacket> {

    private final BlockPos pos;
    private final boolean recipeSelection;
    private final int direction;

    public C2S_AmmoBenchEventPacket() {
        this(null, false, 0);
    }

    private C2S_AmmoBenchEventPacket(BlockPos pos, boolean recipeSelectionMode, int direction) {
        this.pos = pos;
        this.recipeSelection = recipeSelectionMode;
        this.direction = direction;
    }

    public static C2S_AmmoBenchEventPacket craftingEvent(BlockPos pos) {
        return new C2S_AmmoBenchEventPacket(pos, false, 0);
    }

    public static C2S_AmmoBenchEventPacket previousRecipe(BlockPos pos) {
        return new C2S_AmmoBenchEventPacket(pos, true, -1);
    }

    public static C2S_AmmoBenchEventPacket nextRecipe(BlockPos pos) {
        return new C2S_AmmoBenchEventPacket(pos, true, 1);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeBoolean(recipeSelection);
        if (recipeSelection) {
            buffer.writeInt(direction);
        }
    }

    @Override
    public C2S_AmmoBenchEventPacket decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        boolean recipeSelection = buffer.readBoolean();
        int direction = recipeSelection ? buffer.readInt() : 0;
        return new C2S_AmmoBenchEventPacket(pos, recipeSelection, direction);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ServerWorld world = player.getLevel();
        if (!world.isLoaded(pos)) {
            return;
        }
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (!(tileEntity instanceof AmmoBenchTileEntity)) {
            return;
        }
        AmmoBenchTileEntity ammoBench = (AmmoBenchTileEntity) tileEntity;
        if (!recipeSelection) {
            if (ammoBench.isCrafting())
                return;
            ammoBench.startCrafting(player);
        } else {
            if (direction > 0 && ammoBench.canSelectNextRecipe()) {
                ammoBench.setRecipeIndex(ammoBench.getSelectedRecipeIndex() + 1);
            }
            if (direction < 0 && ammoBench.canSelectPreviousRecipe()) {
                ammoBench.setRecipeIndex(ammoBench.getSelectedRecipeIndex() - 1);
            }
            player.server.getPlayerList().broadcastAll(ammoBench.getUpdatePacket());
        }
    }
}
