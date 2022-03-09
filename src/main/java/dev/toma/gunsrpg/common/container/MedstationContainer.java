package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.tileentity.MedstationTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class MedstationContainer extends SkilledWorkbenchContainer<MedstationTileEntity> {

    public MedstationContainer(int windowId, PlayerInventory inventory, MedstationTileEntity tileEntity) {
        super(ModContainers.MEDSTATION.get(), windowId, inventory, tileEntity);
    }

    public MedstationContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowId, inventory, readTileEntity(buffer, inventory));
    }
}
