package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.tileentity.RepairStationTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class RepairStationContainer extends AbstractModContainer<RepairStationTileEntity> {

    public RepairStationContainer(int windowId, PlayerInventory inventory, RepairStationTileEntity tile) {
        super(ModContainers.REPAIR_STATION.get(), windowId, inventory, tile);
        addSlot(this.new InputSlot(tile.getItemHandler(), 0, 44, 26));
        for (int x = 0; x < 3; x++) {
            addSlot(this.new RepairKitSlot(tile.getItemHandler(), 1 + x, 116 + x * 18, 8));
        }
        addPlayerInventory(inventory, 82);
    }

    public RepairStationContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowId, inventory, readTileEntity(buffer, inventory));
    }

    private class InputSlot extends SlotItemHandler {

        public InputSlot(IItemHandler inventory, int index, int xPos, int yPos) {
            super(inventory, index, xPos, yPos);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof GunItem;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            RepairStationContainer.this.broadcastChanges();
        }
    }

    private class RepairKitSlot extends SlotItemHandler {

        public RepairKitSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return stack.getItem() == ModItems.WEAPON_REPAIR_KIT;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            RepairStationContainer.this.broadcastChanges();
        }
    }
}
