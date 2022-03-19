package dev.toma.gunsrpg.common.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public abstract class InventoryTileEntity extends TileEntity {

    protected IItemHandlerModifiable itemHandler;
    private final LazyOptional<IItemHandlerModifiable> inventory;
    protected boolean saveInventory;

    public InventoryTileEntity(TileEntityType<? extends InventoryTileEntity> tileEntityType) {
        super(tileEntityType);
        this.saveInventory = true;
        this.itemHandler = createInventory();
        this.inventory = LazyOptional.of(() -> itemHandler);
    }

    public abstract IItemHandlerModifiable createInventory();

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        if (saveInventory) {
            inventory.ifPresent(handler -> {
                if (handler instanceof INBTSerializable) {
                    CompoundNBT inv = ((INBTSerializable<CompoundNBT>) handler).serializeNBT();
                    nbt.put("inventory", inv);
                }
            });
        }
        write(nbt);
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        if (saveInventory) {
            inventory.ifPresent(handler -> {
                if (handler instanceof INBTSerializable) {
                    CompoundNBT inv = nbt.contains("inventory", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("inventory") : new CompoundNBT();
                    ((INBTSerializable<CompoundNBT>) handler).deserializeNBT(inv);
                }
            });
        }
        read(nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    public void fill(Supplier<ItemStack> itemSupplier) {
        inventory.ifPresent(handler -> {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = itemSupplier.get();
                if (handler.isItemValid(i, stack)) {
                    handler.setStackInSlot(i, stack);
                }
            }
        });
    }

    public void consumeItem(int id) {
        inventory.ifPresent(handler -> handler.getStackInSlot(id).shrink(1));
    }

    public void clear() {
        fill(() -> ItemStack.EMPTY);
    }

    public boolean isEmpty() {
        LazyOptional<IItemHandlerModifiable> inv = getInventory();
        if (inv.isPresent()) {
            IItemHandlerModifiable handler = inv.orElse(null);
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty())
                    return false;
            }
        }
        return true;
    }

    public IItemHandlerModifiable getItemHandler() {
        return itemHandler;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventory.cast();
        }
        return super.getCapability(cap, side);
    }

    public LazyOptional<IItemHandlerModifiable> getInventory() {
        LazyOptional<IItemHandler> capability = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        return capability.cast();
    }

    public void write(CompoundNBT nbt) {
    }

    public void read(CompoundNBT nbt) {
    }
}
