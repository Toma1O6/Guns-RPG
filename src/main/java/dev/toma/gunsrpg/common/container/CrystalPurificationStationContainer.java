package dev.toma.gunsrpg.common.container;

import com.mojang.datafixers.util.Pair;
import dev.toma.gunsrpg.common.init.ClientRegistry;
import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.init.ModTags;
import dev.toma.gunsrpg.common.item.perk.PerkVariant;
import dev.toma.gunsrpg.common.tileentity.CrystalPurificationStationTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrystalPurificationStationContainer extends AbstractModContainer<CrystalPurificationStationTileEntity> {

    public CrystalPurificationStationContainer(int windowId, PlayerInventory inventory, CrystalPurificationStationTileEntity tile) {
        super(ModContainers.CRYSTAL_PURIFICATION.get(), windowId, inventory, tile);
        IItemHandler handler = tile.getItemHandler();
        addSlot(new OutputSlot(handler, 0, 71, 52));
        addSlot(new VariantRestrictedSlot(handler, 1, 71, 10, ModTags.Items.CRYSTAL, ClientRegistry.EMPTY_CRYSTAL_SLOT));
        for (int y = 0; y < 3; y++) {
            addSlot(new VariantRestrictedSlot(handler, 2 + y, 102, 13 + y * 18, ModTags.Items.ORB_OF_PURITY, ClientRegistry.EMPTY_PURITY_SLOT));
        }
        addPlayerInventory(inventory, 82);
    }

    public CrystalPurificationStationContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowId, inventory, readTileEntity(buffer, inventory));
    }

    private class VariantRestrictedSlot extends SlotItemHandler {

        private final ITag<Item> requiredItem;
        private final ResourceLocation location;

        public VariantRestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, ITag<Item> requiredItem, ResourceLocation location) {
            super(itemHandler, index, xPosition, yPosition);
            this.requiredItem = requiredItem;
            this.location = location;
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

        @OnlyIn(Dist.CLIENT)
        @Nullable
        @Override
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return Pair.of(PlayerContainer.BLOCK_ATLAS, location);
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
