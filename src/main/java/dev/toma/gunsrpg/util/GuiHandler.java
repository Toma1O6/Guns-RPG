package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.client.gui.GuiAirdrop;
import dev.toma.gunsrpg.client.gui.GuiBlastFurnace;
import dev.toma.gunsrpg.client.gui.GuiDeathCrate;
import dev.toma.gunsrpg.client.gui.GuiSmithingTable;
import dev.toma.gunsrpg.common.container.ContainerAirdrop;
import dev.toma.gunsrpg.common.container.ContainerBlastFurnace;
import dev.toma.gunsrpg.common.container.ContainerDeathCrate;
import dev.toma.gunsrpg.common.container.ContainerSmithingTable;
import dev.toma.gunsrpg.common.tileentity.TileEntityAirdrop;
import dev.toma.gunsrpg.common.tileentity.TileEntityBlastFurnace;
import dev.toma.gunsrpg.common.tileentity.TileEntityDeathCrate;
import dev.toma.gunsrpg.common.tileentity.TileEntitySmithingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

    public static final int BLAST_FURNACE = 0;
    public static final int AIRDROP = 1;
    public static final int SMITHING_TABLE = 2;
    public static final int DEATH_CRATE = 3;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BLAST_FURNACE: return new ContainerBlastFurnace(player.inventory, (TileEntityBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)));
            case AIRDROP: return new ContainerAirdrop(player.inventory, (TileEntityAirdrop) world.getTileEntity(new BlockPos(x, y, z)));
            case SMITHING_TABLE: return new ContainerSmithingTable(player, (TileEntitySmithingTable) world.getTileEntity(new BlockPos(x, y, z)));
            case DEATH_CRATE: return new ContainerDeathCrate(player.inventory, (TileEntityDeathCrate) world.getTileEntity(new BlockPos(x, y, z)));
            default: return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BLAST_FURNACE: return new GuiBlastFurnace((TileEntityBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
            case AIRDROP: return new GuiAirdrop(player.inventory, (TileEntityAirdrop) world.getTileEntity(new BlockPos(x, y, z)));
            case SMITHING_TABLE: return new GuiSmithingTable(player, (TileEntitySmithingTable) world.getTileEntity(new BlockPos(x, y, z)));
            case DEATH_CRATE: return new GuiDeathCrate(player.inventory, (TileEntityDeathCrate) world.getTileEntity(new BlockPos(x, y, z)));
            default: return null;
        }
    }
}
