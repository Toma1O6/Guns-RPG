package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.tileentity.TileEntityBlastFurnace;
import dev.toma.gunsrpg.util.recipes.BlastFurnaceRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBlastFurnace extends Container {

    private final TileEntityBlastFurnace blastFurnace;

    public ContainerBlastFurnace(InventoryPlayer player, TileEntityBlastFurnace blastFurnace) {
        this.blastFurnace = blastFurnace;
        this.addSlotToContainer(new Slot(blastFurnace, 0, 56, 17));
        this.addSlotToContainer(new Slot(blastFurnace, 2, 56, 53) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Items.COAL;
            }
        });
        this.addSlotToContainer(new Slot(blastFurnace, 1, 116, 35) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(player, k, 8 + k * 18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(index);
        if(slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();
            if(index >= 0 && index <= 2) {
                if(!this.mergeItemStack(stack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemStack);
            } else if(index >= 3 && index <= 38) {
                if(stack.getItem() == Items.COAL) {
                    if(!mergeItemStack(stack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onSlotChange(stack, itemStack);
                } else if(BlastFurnaceRecipe.hasRecipeFor(stack)) {
                    if(!mergeItemStack(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onSlotChange(stack, itemStack);
                } else return ItemStack.EMPTY;
            }
        }
        return itemStack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
