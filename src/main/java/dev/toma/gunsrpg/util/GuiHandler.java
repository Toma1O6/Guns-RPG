package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.client.gui.GuiBlastFurnace;
import dev.toma.gunsrpg.common.container.ContainerBlastFurnace;
import dev.toma.gunsrpg.common.tileentity.TileEntityBlastFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

    public static int BLAST_FURNACE = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0: return new ContainerBlastFurnace(player.inventory, (TileEntityBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)));
            default: return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0: return new GuiBlastFurnace((TileEntityBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
            default: return null;
        }
    }
}
