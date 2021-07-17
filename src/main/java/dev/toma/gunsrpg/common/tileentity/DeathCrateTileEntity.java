package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.GRPGTileEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class DeathCrateTileEntity extends InventoryTileEntity {

    public DeathCrateTileEntity() {
        this(GRPGTileEntities.DEATH_CRATE.get());
    }

    protected DeathCrateTileEntity(TileEntityType<? extends DeathCrateTileEntity> type) {
        super(type);
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(45);
    }

    public void fillInventory(PlayerEntity player) {
        int s = 0;
        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
            ItemStack stack = player.inventory.getItem(i);
            if (!stack.isEmpty()) {
                setItem(s++, stack.copy());
            }
        }
        player.inventory.clearContent();
    }
}
