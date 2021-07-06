package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.GRPGContainers;
import dev.toma.gunsrpg.common.tileentity.DeathCrateTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

public class DeathCrateContainer extends AbstractModContainer<DeathCrateTileEntity> {

    public DeathCrateContainer(int windowID, PlayerInventory inventory, DeathCrateTileEntity tileEntity) {
        super(GRPGContainers.DEATH_CRATE.get(), windowID, inventory, tileEntity);
        tileEntity.getInventory().ifPresent(handler -> {
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 9; x++) {
                    addSlot(new SlotItemHandler(handler, x + (y * 9), 8 + x * 18, 20 + y * 18));
                }
            }
        });
        addPlayerInventory(inventory, 126);
    }

    public DeathCrateContainer(int windowID, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowID, inventory, readTileEntity(buffer, inventory));
    }
}
