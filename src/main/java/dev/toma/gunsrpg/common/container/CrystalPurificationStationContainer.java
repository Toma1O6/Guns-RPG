package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.init.ModTags;
import dev.toma.gunsrpg.common.item.perk.PerkVariant;
import dev.toma.gunsrpg.common.tileentity.CrystalPurificationStationTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CrystalPurificationStationContainer extends AbstractModContainer<CrystalPurificationStationTileEntity> {

    public CrystalPurificationStationContainer(int windowId, PlayerInventory inventory, CrystalPurificationStationTileEntity tile) {
        super(ModContainers.CRYSTAL_PURIFICATION.get(), windowId, inventory, tile);
        IItemHandler handler = tile.getItemHandler();
        addSlot(new OutputSlot(handler, 0, 71, 52));
        addSlot(new VariantRestrictedSlot(handler, 1, 71, 10, ModTags.Items.CRYSTAL));
        for (int y = 0; y < 3; y++) {
            addSlot(new VariantRestrictedSlot(handler, 2 + y, 102, 13 + y * 18, ModTags.Items.ORB_OF_PURITY));
        }
        addPlayerInventory(inventory, 82);
    }

    public CrystalPurificationStationContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowId, inventory, readTileEntity(buffer, inventory));
    }

    private class VariantRestrictedSlot extends SlotItemHandler {

        private final ITag<Item> requiredItem;

        public VariantRestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, ITag<Item> requiredItem) {
            super(itemHandler, index, xPosition, yPosition);
            this.requiredItem = requiredItem;
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            if (!stack.getItem().is(requiredItem)) return false;
            CrystalPurificationStationTileEntity tile = CrystalPurificationStationContainer.this.tileEntity;
            PerkVariant variant = tile.getItemVariant(stack);
            PerkVariant target = tile.getTargetedPerkVariant();
            return target == null || variant == target;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    private static class OutputSlot extends SlotItemHandler {

        public OutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return false;
        }
    }
}
