package dev.toma.gunsrpg.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class TileEntityDeathCrate extends IInventoryFactory {

    @Override
    public int getSizeInventory() {
        return 45;
    }

    @Override
    public String getName() {
        return "container.death_crate";
    }

    public void fillInventory(EntityPlayer player) {
        int s = 0;
        for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(!stack.isEmpty()) {
                setInventorySlotContents(s++, stack.copy());
            }
        }
        player.inventory.clear();
    }
}
