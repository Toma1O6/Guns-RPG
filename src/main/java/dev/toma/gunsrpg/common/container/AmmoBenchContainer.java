package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.tileentity.AmmoBenchTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class AmmoBenchContainer extends AbstractModContainer<AmmoBenchTileEntity> {

    public AmmoBenchContainer(int windowId, PlayerInventory inventory, AmmoBenchTileEntity tileEntity) {
        super(ModContainers.AMMO_BENCH_CONTAINER.get(), windowId, inventory, tileEntity);

        IItemHandler itemHandler = tileEntity.getItemHandler();
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 3; x++) {
                // Input
                addSlot(new SlotItemHandler(itemHandler, (y * 3) + x ,8 + x * 18, 32 + y * 18));
                // Output
                addSlot(new OutputSlot(itemHandler, 6 + (y * 3) + x, 116 + x * 18, 32 + y * 18));
            }
        }
        // Player inventory
        addPlayerInventory(inventory, 93);

        addSlotListener(new Listener());
    }

    public AmmoBenchContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowId, inventory, readTileEntity(buffer, inventory));
    }

    private static final class OutputSlot extends SlotItemHandler {

        public OutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return false;
        }
    }

    private final class Listener implements IContainerListener {

        @Override
        public void refreshContainer(Container container, NonNullList<ItemStack> itemStacks) {
        }

        @Override
        public void slotChanged(Container container, int slotIndex, ItemStack itemStack) {
            AmmoBenchContainer.this.getTileEntity().onSlotChanged();
        }

        @Override
        public void setContainerData(Container container, int key, int data) {
        }
    }
}
