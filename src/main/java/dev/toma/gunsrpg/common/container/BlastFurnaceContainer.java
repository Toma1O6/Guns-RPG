package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.GRPGContainers;
import dev.toma.gunsrpg.common.tileentity.BlastFurnaceTileEntity;
import dev.toma.gunsrpg.util.recipes.BlastFurnaceRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

public class BlastFurnaceContainer extends AbstractModContainer<BlastFurnaceTileEntity> {

    public BlastFurnaceContainer(int windowID, PlayerInventory inventory, BlastFurnaceTileEntity tileEntity) {
        super(GRPGContainers.BLAST_FURNACE.get(), windowID, inventory, tileEntity);
        tileEntity.getInventory().ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 0, 56, 17));
            addSlot(new SlotItemHandler(handler, 1, 116, 35) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
            addSlot(new SlotItemHandler(handler, 2, 56, 53) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() == Items.COAL;
                }
            });
        });
        addPlayerInventory(inventory, 84);
    }

    public BlastFurnaceContainer(int windowID, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowID, inventory, readTileEntity(buffer, inventory));
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(index);
        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemStack = stack.copy();
            if(index >= 0 && index <= 2) {
                if(!this.moveItemStackTo(stack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemStack);
            } else if(index >= 3 && index <= 38) {
                if(stack.getItem() == Items.COAL) {
                    if(!moveItemStackTo(stack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(stack, itemStack);
                } else if(BlastFurnaceRecipe.hasRecipeFor(stack)) {
                    if(!moveItemStackTo(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(stack, itemStack);
                } else return ItemStack.EMPTY;
            }
        }
        return itemStack;
    }
}
