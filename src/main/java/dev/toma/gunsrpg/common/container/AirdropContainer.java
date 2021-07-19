package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.tileentity.AirdropTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

public class AirdropContainer extends AbstractModContainer<AirdropTileEntity> {

    public AirdropContainer(int windowID, PlayerInventory inventory, AirdropTileEntity tileEntity) {
        super(ModContainers.AIRDROP.get(), windowID, inventory, tileEntity);
        tileEntity.getInventory().ifPresent(handler -> {
            for (int x = 0; x < 9; x++) {
                addSlot(new SlotItemHandler(handler, x, 8 + x * 18, 20));
            }
        });
        addPlayerInventory(inventory, 51);
    }

    public AirdropContainer(int windowID, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowID, inventory, readTileEntity(buffer, inventory));
    }
}
