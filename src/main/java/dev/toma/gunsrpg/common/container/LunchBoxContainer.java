package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class LunchBoxContainer extends GenericStorageContainer {

    public LunchBoxContainer(PlayerInventory inventory, int windowId) {
        super(ModContainers.LUNCH_BOX.get(), inventory, windowId);
    }

    public LunchBoxContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(inventory, windowId);
    }
}
