package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.tileentity.TileEntitySmithingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSmithingTable extends ModContainer {

    final TileEntitySmithingTable tileEntitySmithingTable;

    public ContainerSmithingTable(EntityPlayer player, TileEntitySmithingTable tileEntity) {
        super(tileEntity);
        this.tileEntitySmithingTable = tileEntity;
        // input
        addSlots(tileEntity, 3, 3, 26, 8);
        // player
        addPlayerInventory(player.inventory, 90);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();
            if(index >= 0 && index <= 8) {
                if(!mergeItemStack(stack, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemStack);
            } else if(index >= 9 && index <= 44) {
                if(!mergeItemStack(stack, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemStack);
            }
        }
        return itemStack;
    }

    /*@Override
    public void onContainerClosed(EntityPlayer playerIn) {
        if(!tileEntitySmithingTable.getWorld().isRemote) {
            World world = tileEntitySmithingTable.getWorld();
            BlockPos pos = tileEntitySmithingTable.getPos();
            for(int i = 0; i < tileEntitySmithingTable.getSizeInventory(); i++) {
                ItemStack stack = tileEntitySmithingTable.getStackInSlot(i);
                if(!stack.isEmpty()) {
                    world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, stack.copy()));
                    tileEntitySmithingTable.removeStackFromSlot(i);
                }
            }
        }
    }*/
}
