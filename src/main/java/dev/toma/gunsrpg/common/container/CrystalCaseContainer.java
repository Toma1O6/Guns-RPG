package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class CrystalCaseContainer extends GenericStorageContainer {

    public CrystalCaseContainer(PlayerInventory inventory, int id) {
        super(ModContainers.CRYSTAL_CASE.get(), inventory, id);
    }

    public CrystalCaseContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(inventory, id);
    }
}
