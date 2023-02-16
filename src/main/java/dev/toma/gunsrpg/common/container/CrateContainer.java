package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.tileentity.ILootGenerator;
import dev.toma.gunsrpg.common.tileentity.InventoryTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class CrateContainer<T extends InventoryTileEntity & ILootGenerator> extends AbstractModContainer<T> {

    public CrateContainer(int windowID, PlayerInventory inventory, T tileEntity) {
        super(ModContainers.CRATE.get(), windowID, inventory, tileEntity);
        tileEntity.getInventory().ifPresent(handler -> {
            for (int x = 0; x < 9; x++) {
                addSlot(new SlotItemHandler(handler, x, 8 + x * 18, 20));
            }
        });
        addPlayerInventory(inventory, 51);
    }

    public CrateContainer(int windowID, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowID, inventory, readTileEntity(buffer, inventory));
    }

    @Override
    public void removed(PlayerEntity player) {
        super.removed(player);
        T inventory = this.getTileEntity();
        World world = player.level;
        if (!world.isClientSide && inventory.isEmpty()) {
            BlockPos pos = inventory.getBlockPos();
            world.destroyBlock(pos, true);
        }
    }
}
