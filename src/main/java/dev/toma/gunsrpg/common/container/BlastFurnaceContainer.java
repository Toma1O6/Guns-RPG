package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.tileentity.BlastFurnaceTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;

public class BlastFurnaceContainer extends RecipeBookContainer<IInventory> {

    private final BlastFurnaceTileEntity tileEntity;

    private final World level;
    private final IRecipeType<FurnaceRecipe> recipeType = IRecipeType.SMELTING;
    private final IIntArray data;

    public BlastFurnaceContainer(int windowID, PlayerInventory inventory, BlastFurnaceTileEntity tileEntity) {
        super(ModContainers.BLAST_FURNACE.get(), windowID);
        this.tileEntity = tileEntity;
        this.level = inventory.player.level;
        this.data = tileEntity.getData();
        checkContainerSize(tileEntity, 3);

        addSlot(new Slot(tileEntity, 0, 56, 17));
        addSlot(new FuelSlot(this, tileEntity, 1, 56, 53));
        addSlot(new OutputSlot(inventory.player, tileEntity, 2, 116, 35));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(inventory, 9 + y * 9 + x, 8 + x * 18, 84 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(inventory, x, 8 + x * 18, 142));
        }
    }

    public BlastFurnaceContainer(int windowID, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowID, inventory, AbstractModContainer.readTileEntity(buffer, inventory));
    }

    @Override
    public void fillCraftSlotsStackedContents(RecipeItemHelper itemHelper) {
        tileEntity.fillStackedContents(itemHelper);
    }

    @Override
    public void clearCraftingContent() {
        tileEntity.clearContent();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handlePlacement(boolean flag, IRecipe<?> recipe, ServerPlayerEntity player) {
        new ServerRecipePlacerFurnace<>(this).recipeClicked(player, (IRecipe<IInventory>) recipe, flag);
    }

    @Override
    public boolean recipeMatches(IRecipe<? super IInventory> recipe) {
        return recipe.matches(tileEntity, level);
    }

    @Override
    public int getResultSlotIndex() {
        return 2;
    }

    @Override
    public int getGridWidth() {
        return 1;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return tileEntity.stillValid(player);
    }

    @Override
    public RecipeBookCategory getRecipeBookType() {
        return RecipeBookCategory.FURNACE;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 2) {
                if (!moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                if (isValidInput(itemstack1)) {
                    if (!moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemstack1)) {
                    if (!moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!moveItemStackTo(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public boolean isLit() {
        return data.get(0) > 0;
    }

    public int getLitProgress() {
        int i = data.get(1);
        if (i == 0) {
            i = 200;
        }
        return data.get(0) * 13 / i;
    }

    public int getBurnProgress() {
        int cookProgress = data.get(2);
        int totalCookProgress = data.get(3);
        return totalCookProgress != 0 && cookProgress != 0 ? cookProgress * 24 / totalCookProgress : 0;
    }

    boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, recipeType) > 0;
    }

    boolean isValidInput(ItemStack stack) {
        return level.getRecipeManager().getRecipeFor(recipeType, new Inventory(stack), level).isPresent();
    }

    private static class OutputSlot extends Slot {
        private final PlayerEntity interactingPlayer;
        private int takeCount;

        OutputSlot(PlayerEntity interactingPlayer, IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.interactingPlayer = interactingPlayer;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack remove(int count) {
            if (hasItem()) {
                takeCount += Math.min(count, getItem().getCount());
            }
            return super.remove(count);
        }

        @Override
        public ItemStack onTake(PlayerEntity player, ItemStack stack) {
            checkTakeAchievements(stack);
            super.onTake(player, stack);
            return stack;
        }

        @Override
        protected void onQuickCraft(ItemStack stack, int count) {
            takeCount += count;
            checkTakeAchievements(stack);
        }

        @Override
        protected void checkTakeAchievements(ItemStack stack) {
            World level = interactingPlayer.level;
            stack.onCraftedBy(level, interactingPlayer, takeCount);
            if (!level.isClientSide && container instanceof BlastFurnaceTileEntity) {
                BlastFurnaceTileEntity tileEntity = (BlastFurnaceTileEntity) container;
                tileEntity.awardUsedRecipeAndAddExp(interactingPlayer);
            }
            takeCount = 0;
            BasicEventHooks.firePlayerSmeltedEvent(interactingPlayer, stack);
        }
    }

    private static class FuelSlot extends Slot {

        private final BlastFurnaceContainer container;

        FuelSlot(BlastFurnaceContainer container, IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.container = container;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return container.isFuel(stack) || isBucket(stack);
        }

        @Override
        public int getMaxStackSize(ItemStack stack) {
            return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
        }

        private static boolean isBucket(ItemStack stack) {
            return stack.getItem() == Items.BUCKET;
        }
    }
}
