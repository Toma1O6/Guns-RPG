package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class SmithingTableContainer extends SkilledWorkbenchContainer<SmithingTableTileEntity> {

    public SmithingTableContainer(int windowID, PlayerInventory inventory, SmithingTableTileEntity tileEntity) {
        super(ModContainers.SMITHING_TABLE.get(), windowID, inventory, tileEntity);
    }

    public SmithingTableContainer(int windowID, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowID, inventory, readTileEntity(buffer, inventory));
    }
}
