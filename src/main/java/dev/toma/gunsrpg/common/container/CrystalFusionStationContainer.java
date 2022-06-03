package dev.toma.gunsrpg.common.container;

import com.mojang.datafixers.util.Pair;
import dev.toma.gunsrpg.common.init.ClientRegistry;
import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.init.ModTags;
import dev.toma.gunsrpg.common.item.perk.PerkVariant;
import dev.toma.gunsrpg.common.tileentity.CrystalFusionStationTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrystalFusionStationContainer extends AbstractModContainer<CrystalFusionStationTileEntity> {

    public CrystalFusionStationContainer(int id, PlayerInventory inventory, CrystalFusionStationTileEntity tile) {
        super(ModContainers.CRYSTAL_FUSE.get(), id, inventory, tile);
        IItemHandler handler = tile.getItemHandler();
        addSlot(new SlotItemHandler(handler, 0, 80, 33) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return false;
            }
        });
        addSlot(new Input(handler, 1, 38, 10));
        addSlot(new Input(handler, 2, 122, 10));
        for (int x = 0; x < 3; x++) {
            addSlot(new Orb(handler, 3 + x, 62 + x * 18, 10));
        }
        addPlayerInventory(inventory, 82);
    }

    public CrystalFusionStationContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, readTileEntity(buffer, inventory));
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    private class Input extends SlotItemHandler {

        public Input(IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            if (!stack.getItem().is(ModTags.Items.CRYSTAL)) return false;
            CrystalFusionStationTileEntity tile = CrystalFusionStationContainer.this.tileEntity;
            PerkVariant variant = tile.getItemVariant(stack);
            return tile.canAddItem(variant);
        }

        @OnlyIn(Dist.CLIENT)
        @Nullable
        @Override
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return Pair.of(PlayerContainer.BLOCK_ATLAS, CrystalStationContainer.EMPTY_CRYSTAL_SLOT);
        }
    }

    private class Orb extends SlotItemHandler {

        public Orb(IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            if (!stack.getItem().is(ModTags.Items.ORB_OF_TRANSMUTATION)) return false;
            CrystalFusionStationTileEntity tile = CrystalFusionStationContainer.this.tileEntity;
            PerkVariant variant = tile.getItemVariant(stack);
            PerkVariant target = tile.getTargetedOrbType();
            return tile.canAddItem(variant) && (target == null || target == variant);
        }

        @OnlyIn(Dist.CLIENT)
        @Nullable
        @Override
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return Pair.of(PlayerContainer.BLOCK_ATLAS, CrystalStationContainer.EMPTY_TRANSMUTATION_SLOT);
        }
    }
}
