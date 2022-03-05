package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.tileentity.CulinaryTableTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class CulinaryTableContainer extends SkilledWorkbenchContainer<CulinaryTableTileEntity> {

    public CulinaryTableContainer(int windowId, PlayerInventory inventory, CulinaryTableTileEntity tileEntity) {
        super(ModContainers.CULINARY_TABLE.get(), windowId, inventory, tileEntity);
    }

    public CulinaryTableContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowId, inventory, readTileEntity(buffer, inventory));
    }
}
