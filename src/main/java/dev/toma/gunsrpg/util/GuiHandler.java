package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.client.gui.GuiAirdrop;
import dev.toma.gunsrpg.client.gui.GuiBlastFurnace;
import dev.toma.gunsrpg.client.gui.GuiDeathCrate;
import dev.toma.gunsrpg.client.gui.GuiSmithingTable;
import dev.toma.gunsrpg.common.container.AirdropContainer;
import dev.toma.gunsrpg.common.container.BlastFurnaceContainer;
import dev.toma.gunsrpg.common.container.DeathCrateContainer;
import dev.toma.gunsrpg.common.container.SmithingTableContainer;
import dev.toma.gunsrpg.common.tileentity.AirdropTileEntity;
import dev.toma.gunsrpg.common.tileentity.BlastFurnaceTileEntity;
import dev.toma.gunsrpg.common.tileentity.DeathCrateTileEntity;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

@Deprecated
public class GuiHandler implements IGuiHandler {

    public static final int BLAST_FURNACE = 0;
    public static final int AIRDROP = 1;
    public static final int SMITHING_TABLE = 2;
    public static final int DEATH_CRATE = 3;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BLAST_FURNACE: return new BlastFurnaceContainer(player.inventory, (BlastFurnaceTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
            case AIRDROP: return new AirdropContainer(player.inventory, (AirdropTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
            case SMITHING_TABLE: return new SmithingTableContainer(player, (SmithingTableTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
            case DEATH_CRATE: return new DeathCrateContainer(player.inventory, (DeathCrateTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
            default: return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BLAST_FURNACE: return new GuiBlastFurnace((BlastFurnaceTileEntity) world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
            case AIRDROP: return new GuiAirdrop(player.inventory, (AirdropTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
            case SMITHING_TABLE: return new GuiSmithingTable(player, (SmithingTableTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
            case DEATH_CRATE: return new GuiDeathCrate(player.inventory, (DeathCrateTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
            default: return null;
        }
    }
}
