package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class AmmoCaseContainer extends GenericStorageContainer {

    public AmmoCaseContainer(PlayerInventory inventory, int windowId) {
        super(ModContainers.AMMO_CASE.get(), inventory, windowId);
    }

    public AmmoCaseContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(inventory, windowId);
    }
}
