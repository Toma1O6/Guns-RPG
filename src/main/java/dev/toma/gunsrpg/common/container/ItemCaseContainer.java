package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ItemCaseContainer extends GenericStorageContainer {

    public ItemCaseContainer(PlayerInventory inventory, int windowId) {
        super(ModContainers.ITEM_CASE.get(), inventory, windowId);
    }

    public ItemCaseContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(inventory, windowId);
    }
}
