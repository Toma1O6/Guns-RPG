package com.wf.firearms.gunsmith;

import com.wf.firearms.registry.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class GunsmithMenu extends AbstractContainerMenu {
    public static final int CRAFT_START = 0;
    public static final int CRAFT_SLOTS = 9;
    public static final int OUTPUT_SLOT = 9;
    public static final int PLAYER_INV_START = 10;

    private final GunsmithTableBlockEntity blockEntity;
    private final Player player;

    public GunsmithMenu(int id, Inventory playerInv, GunsmithTableBlockEntity be) {
        super(ModMenuTypes.GUNSMITH_TABLE.get(), id);
        this.blockEntity = be;
        this.player = playerInv.player;

        if (be.getItems().getSlots() < CRAFT_SLOTS) {
            be.getItems().setSize(CRAFT_SLOTS);
        }

        // 槽位对齐 Guns RPG SkilledWorkbenchContainer（3×3 自 8,8；背包自 y=90）
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int idx = col + row * 3;
                addSlot(new SlotItemHandler(be.getItems(), idx, 8 + col * 18, 8 + row * 18));
            }
        }
        addSlot(new GunsmithPreviewSlot(new SimpleContainer(1), be, 124, 26, player));

        int invTop = 90;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, invTop + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, 8 + col * 18, invTop + 58));
        }
        addDataSlots(be.getData());
    }

    public GunsmithTableBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public static GunsmithMenu fromNetwork(int id, Inventory inv, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = inv.player.level().getBlockEntity(pos);
        if (be instanceof GunsmithTableBlockEntity table) {
            return new GunsmithMenu(id, inv, table);
        }
        throw new IllegalStateException("No gunsmith table at " + pos);
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity != null && blockEntity.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            copy = stack.copy();
            if (index == OUTPUT_SLOT) {
                if (!moveItemStackTo(stack, PLAYER_INV_START, PLAYER_INV_START + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, copy);
            } else if (index >= PLAYER_INV_START) {
                if (!moveItemStackTo(stack, CRAFT_START, CRAFT_START + CRAFT_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < CRAFT_SLOTS) {
                if (!moveItemStackTo(stack, PLAYER_INV_START, PLAYER_INV_START + 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return copy;
    }

    private static class GunsmithPreviewSlot extends Slot {
        private final GunsmithTableBlockEntity be;
        private final Player player;

        GunsmithPreviewSlot(SimpleContainer dummy, GunsmithTableBlockEntity be, int x, int y, Player player) {
            super(dummy, 0, x, y);
            this.be = be;
            this.player = player;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack getItem() {
            return be.previewFor(player);
        }

        @Override
        public boolean hasItem() {
            return !getItem().isEmpty();
        }

        @Override
        public void set(ItemStack stack) {}

        @Override
        public ItemStack remove(int amount) {
            if (!mayPickup(player)) {
                return ItemStack.EMPTY;
            }
            ItemStack preview = getItem();
            if (preview.isEmpty() || !be.tryCraft(player)) {
                return ItemStack.EMPTY;
            }
            return preview.copy();
        }

        @Override
        public boolean mayPickup(Player player) {
            GunsmithRecipe recipe = GunsmithTableBlockEntity.findRecipe(player.level(), be);
            return recipe != null && recipe.canProduce(player);
        }
    }
}
