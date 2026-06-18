package com.wf.firearms.repair;

import com.wf.firearms.combat.WeaponRepairService;
import com.wf.firearms.registry.ModItems;
import com.wf.firearms.registry.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class RepairStationMenu extends AbstractContainerMenu {
    public static final int SLOT_WEAPON = 0;
    public static final int SLOT_KIT_START = 1;
    public static final int SLOT_KIT_COUNT = 3;
    public static final int PLAYER_INV_START = 4;

    private final RepairStationBlockEntity blockEntity;

    public RepairStationMenu(int id, Inventory playerInv, RepairStationBlockEntity be) {
        super(ModMenuTypes.REPAIR_STATION.get(), id);
        this.blockEntity = be;

        addSlot(new WeaponSlot(be.getItems(), SLOT_WEAPON, 44, 26));
        for (int i = 0; i < SLOT_KIT_COUNT; i++) {
            addSlot(new RepairKitSlot(be.getItems(), SLOT_KIT_START + i, 116 + i * 18, 8));
        }

        int invTop = 82;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, invTop + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, 8 + col * 18, invTop + 58));
        }
    }

    public RepairStationBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public static RepairStationMenu fromNetwork(int id, Inventory inv, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = inv.player.level().getBlockEntity(pos);
        if (be instanceof RepairStationBlockEntity station) {
            return new RepairStationMenu(id, inv, station);
        }
        throw new IllegalStateException("No repair station at " + pos);
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        copy = stack.copy();
        if (index < PLAYER_INV_START) {
            if (!moveItemStackTo(stack, PLAYER_INV_START, PLAYER_INV_START + 36, true)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= PLAYER_INV_START) {
            if (WeaponRepairService.isRepairableWeapon(stack)) {
                if (!moveItemStackTo(stack, SLOT_WEAPON, SLOT_WEAPON + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (stack.is(ModItems.WEAPON_REPAIR_KIT.get())) {
                if (!moveItemStackTo(stack, SLOT_KIT_START, SLOT_KIT_START + SLOT_KIT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return copy;
    }

    private static class WeaponSlot extends SlotItemHandler {
        WeaponSlot(net.minecraftforge.items.IItemHandler inv, int index, int x, int y) {
            super(inv, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return WeaponRepairService.isRepairableWeapon(stack);
        }
    }

    private static class RepairKitSlot extends SlotItemHandler {
        RepairKitSlot(net.minecraftforge.items.IItemHandler inv, int index, int x, int y) {
            super(inv, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ModItems.WEAPON_REPAIR_KIT.get());
        }
    }
}
