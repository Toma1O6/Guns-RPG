package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class MedsCaseContainer extends GenericStorageContainer {

    public MedsCaseContainer(PlayerInventory inventory, int windowId) {
        super(ModContainers.MEDS_CASE.get(), inventory, windowId);
    }

    public MedsCaseContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(inventory, windowId);
    }
}
