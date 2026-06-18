package com.wf.firearms.medical;

import com.wf.firearms.registry.ModBlockEntities;
import com.wf.firearms.registry.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class MedicalStationTableBlockEntity extends BaseContainerBlockEntity implements Container {
    public static final int CRAFT_SLOTS = 9;

    private final ItemStackHandler items = new ItemStackHandler(CRAFT_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return 0;
        }

        @Override
        public void set(int index, int value) {}

        @Override
        public int getCount() {
            return 0;
        }
    };

    public MedicalStationTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MEDICAL_STATION.get(), pos, state);
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public ContainerData getData() {
        return data;
    }

    @Nullable
    public ItemStack previewFor(@Nullable Player player) {
        if (level == null || player == null) {
            return ItemStack.EMPTY;
        }
        MedicalStationRecipe recipe = findRecipe(level, this);
        if (recipe == null) {
            return ItemStack.EMPTY;
        }
        return recipe.resultFor(player);
    }

    @Nullable
    public static MedicalStationRecipe findRecipe(Level level, Container grid) {
        if (level == null) {
            return null;
        }
        for (MedicalStationRecipe recipe :
                level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.MEDICAL_STATION.get())) {
            if (recipe.matches(grid, level)) {
                return recipe;
            }
        }
        return null;
    }

    public boolean tryCraft(Player player) {
        if (level == null || level.isClientSide) {
            return false;
        }
        MedicalStationRecipe recipe = findRecipe(level, this);
        if (recipe == null || !recipe.canProduce(player)) {
            return false;
        }
        ItemStack out = recipe.resultFor(player);
        if (out.isEmpty()) {
            return false;
        }
        recipe.consumeMatches(this);
        if (!player.getInventory().add(out)) {
            player.drop(out, false);
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
        if (amount <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = getItem(slot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack result = stack.split(amount);
        setItem(slot, stack);
        setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot);
        if (!stack.isEmpty()) {
            setItem(slot, ItemStack.EMPTY);
        }
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.setStackInSlot(slot, stack);
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
    public void clearContent() {
        for (int i = 0; i < items.getSlots(); i++) {
            items.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.gunsrpg.medical_station");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new MedicalStationMenu(id, inv, this);
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
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}
