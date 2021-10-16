package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class GrenadeCaseContainer extends GenericStorageContainer {

    public GrenadeCaseContainer(PlayerInventory inventory, int windowId) {
        super(ModContainers.GRENADE_CASE.get(), inventory, windowId);
    }

    public GrenadeCaseContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(inventory, windowId);
    }
}
