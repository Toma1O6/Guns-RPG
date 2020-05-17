package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.tileentity.TileEntityAirdrop;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerAirdrop extends ModContainer {

    public ContainerAirdrop(InventoryPlayer player, TileEntityAirdrop tileEntityAirdrop) {
        super(tileEntityAirdrop);
        for(int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(tileEntityAirdrop, x, 8 + x * 18, 20));
        }
        this.addPlayerInventory(player, 51);
    }
}
