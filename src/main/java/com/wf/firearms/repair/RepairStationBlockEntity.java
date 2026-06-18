package com.wf.firearms.repair;



import com.wf.firearms.combat.WeaponRepairService;

import com.wf.firearms.registry.ModBlockEntities;

import com.wf.firearms.registry.ModItems;

import net.minecraft.core.BlockPos;

import net.minecraft.nbt.CompoundTag;

import net.minecraft.network.chat.Component;

import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;

import net.minecraft.world.entity.player.Inventory;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.inventory.AbstractContainerMenu;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;

import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.items.ItemStackHandler;

import org.jetbrains.annotations.Nullable;



public class RepairStationBlockEntity extends BaseContainerBlockEntity {

    public static final int SLOT_WEAPON = 0;

    public static final int SLOT_KIT_START = 1;

    public static final int SLOT_COUNT = 4;



    private final ItemStackHandler items = new ItemStackHandler(SLOT_COUNT) {

        @Override

        protected void onContentsChanged(int slot) {

            setChanged();

        }

    };



    public RepairStationBlockEntity(BlockPos pos, BlockState state) {

        super(ModBlockEntities.REPAIR_STATION.get(), pos, state);

    }



    public ItemStackHandler getItems() {

        return items;

    }



    private int findRepairKitSlot() {

        for (int i = SLOT_KIT_START; i < SLOT_COUNT; i++) {

            ItemStack stack = items.getStackInSlot(i);

            if (!stack.isEmpty() && stack.is(ModItems.WEAPON_REPAIR_KIT.get())) {

                return i;

            }

        }

        return -1;

    }



    @Nullable

    private ItemStack findRepairKit() {

        int slot = findRepairKitSlot();

        return slot >= 0 ? items.getStackInSlot(slot) : null;

    }



    public boolean canRepair(Player player) {

        ItemStack weapon = items.getStackInSlot(SLOT_WEAPON);

        ItemStack kit = findRepairKit();

        if (kit == null) {

            return false;

        }

        return WeaponRepairService.weaponKey(weapon)

                .map(key -> WeaponRepairService.canRepair(player, weapon, key, kit))

                .orElse(false);

    }



    public boolean tryRepair(Player player) {

        if (level == null || level.isClientSide || !canRepair(player)) {

            return false;

        }

        ItemStack weapon = items.getStackInSlot(SLOT_WEAPON);

        int kitSlot = findRepairKitSlot();

        if (kitSlot < 0) {

            return false;

        }

        ItemStack kit = items.getStackInSlot(kitSlot);

        var key = WeaponRepairService.weaponKey(weapon);

        if (key.isEmpty()) {

            return false;

        }

        WeaponRepairService.performRepair(player, weapon, key.get(), kit);

        if (!player.getAbilities().instabuild) {

            items.setStackInSlot(kitSlot, kit.isEmpty() ? ItemStack.EMPTY : kit);

        }

        setChanged();

        return true;

    }



    @Override

    public int getContainerSize() {

        return items.getSlots();

    }



    @Override

    public boolean isEmpty() {

        for (int i = 0; i < items.getSlots(); i++) {

            if (!items.getStackInSlot(i).isEmpty()) {

                return false;

            }

        }

        return true;

    }



    @Override

    public ItemStack getItem(int slot) {

        return items.getStackInSlot(slot);

    }



    @Override

    public ItemStack removeItem(int slot, int amount) {

        return items.extractItem(slot, amount, false);

    }



    @Override

    public ItemStack removeItemNoUpdate(int slot) {

        return items.extractItem(slot, items.getStackInSlot(slot).getCount(), false);

    }



    @Override

    public void setItem(int slot, ItemStack stack) {

        items.setStackInSlot(slot, stack);

    }



    @Override

    public void clearContent() {

        for (int i = 0; i < items.getSlots(); i++) {

            items.setStackInSlot(i, ItemStack.EMPTY);

        }

    }



    @Override

    public void load(CompoundTag tag) {

        super.load(tag);

        if (tag.contains("Items", net.minecraft.nbt.Tag.TAG_COMPOUND)) {

            items.deserializeNBT(tag.getCompound("Items"));

        }

    }



    @Override

    protected void saveAdditional(CompoundTag tag) {

        super.saveAdditional(tag);

        tag.put("Items", items.serializeNBT());

    }



    @Override

    public void setChanged() {

        super.setChanged();

        if (level != null && !level.isClientSide) {

            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);

        }

    }



    @Override

    public boolean stillValid(Player player) {

        return player.distanceToSqr(

                        worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5)

                <= 64;

    }



    @Override

    protected Component getDefaultName() {

        return Component.translatable("container.gunsrpg.repair_station");

    }



    @Nullable

    @Override

    protected AbstractContainerMenu createMenu(int id, Inventory inv) {

        return new RepairStationMenu(id, inv, this);

    }



    @Override

    public ClientboundBlockEntityDataPacket getUpdatePacket() {

        return ClientboundBlockEntityDataPacket.create(this);

    }



    @Override

    public CompoundTag getUpdateTag() {

        return saveWithoutMetadata();

    }

}


