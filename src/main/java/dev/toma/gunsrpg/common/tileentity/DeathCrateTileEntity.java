package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.ModBlockEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class DeathCrateTileEntity extends InventoryTileEntity {

    private String victimName = "Unknown";

    public DeathCrateTileEntity() {
        this(ModBlockEntities.DEATH_CRATE.get());
    }

    protected DeathCrateTileEntity(TileEntityType<? extends DeathCrateTileEntity> type) {
        super(type);
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(45);
    }

    public String getVictimName() {
        return victimName;
    }

    public void fillInventory(PlayerEntity player) {
        int s = 0;
        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
            ItemStack stack = player.inventory.getItem(i);
            if (!stack.isEmpty()) {
                itemHandler.setStackInSlot(s++, stack.copy());
            }
        }
        this.victimName = player.getName().getString();
        player.inventory.clearContent();
    }

    @Override
    public void write(CompoundNBT nbt) {
        nbt.putString("victim", victimName);
    }

    @Override
    public void read(CompoundNBT nbt) {
        victimName = nbt.contains("victim", Constants.NBT.TAG_STRING) ? nbt.getString("victim") : "Unknown";
    }
}
