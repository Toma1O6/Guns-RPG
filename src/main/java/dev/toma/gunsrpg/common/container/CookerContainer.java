package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.common.tileentity.CookerTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class CookerContainer extends AbstractModContainer<CookerTileEntity> {

    public CookerContainer(int windowID, PlayerInventory playerInventory, CookerTileEntity tileEntity) {
        super(ModContainers.COOKER.get(), windowID, playerInventory, tileEntity);
        checkContainerSize(tileEntity, 3);

        addSlot(new Slot(tileEntity, 0, 56, 17));
        addSlot(this.new FuelSlot(tileEntity, 1, 56, 53));
        addSlot(new OutputSlot(playerInventory.player, tileEntity, 2, 116, 35));

        addPlayerInventory(playerInventory, 84);
    }

    public CookerContainer(int windowId, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(windowId, playerInventory, readTileEntity(buffer, playerInventory));
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
                if (this.isFuel(itemstack1)) {
                    if (!moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!moveItemStackTo(itemstack1, 0, 1, false)) {
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
        return tileEntity.isLit();
    }

    public int getLitProgress() {
        int i = tileEntity.getLitDuration();
        if (i == 0) {
            i = 200;
        }
        return tileEntity.getLitTime() * 13 / i;
    }

    public int getBurnProgress() {
        int cookProgress = tileEntity.getCookingProgress();
        int totalCookProgress = tileEntity.getCookingTotalTime();
        return totalCookProgress != 0 && cookProgress != 0 ? cookProgress * 24 / totalCookProgress : 0;
    }

    private boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, ModRecipeTypes.COOKING_RECIPE_TYPE) > 0;
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
            if (!level.isClientSide && container instanceof CookerTileEntity) {
                CookerTileEntity tileEntity = (CookerTileEntity) container;
                tileEntity.awardUsedRecipes(interactingPlayer);
            }
            takeCount = 0;
        }
    }

    private class FuelSlot extends Slot {

        FuelSlot(IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return CookerContainer.this.isFuel(stack);
        }
    }
}
