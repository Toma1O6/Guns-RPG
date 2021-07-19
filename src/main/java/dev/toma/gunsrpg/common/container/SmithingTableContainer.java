package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

public class SmithingTableContainer extends AbstractModContainer<SmithingTableTileEntity> {

    public SmithingTableContainer(int windowID, PlayerInventory inventory, SmithingTableTileEntity tileEntity) {
        super(ModContainers.SMITHING_TABLE.get(), windowID, inventory, tileEntity);
        tileEntity.getInventory().ifPresent(handler -> {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    addSlot(new SlotItemHandler(handler, x + y * 9, 26 + x * 18, 8 + y * 18));
                }
            }
        });
        addPlayerInventory(inventory, 90);
    }

    public SmithingTableContainer(int windowID, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowID, inventory, readTileEntity(buffer, inventory));
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = getSlot(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemStack = stack.copy();
            if (index >= 0 && index <= 8) {
                if (!moveItemStackTo(stack, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemStack);
            } else if (index >= 9 && index <= 44) {
                if (!moveItemStackTo(stack, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemStack);
            }
        }
        return itemStack;
    }
}
