package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.tileentity.TileEntityDeathCrate;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerDeathCrate extends ModContainer {

    public ContainerDeathCrate(InventoryPlayer player, TileEntityDeathCrate deathCrate) {
        super(deathCrate);
        addSlots(deathCrate, 5, 9, 8, 20);
        addPlayerInventory(player, 126);
    }
}
