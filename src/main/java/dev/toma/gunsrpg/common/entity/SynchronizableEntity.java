package dev.toma.gunsrpg.common.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public interface SynchronizableEntity extends IEntityAdditionalSpawnData {

    CompoundNBT serializeNetworkData();

    void deserializeNetworkData(CompoundNBT tag);

    @Override
    default void writeSpawnData(PacketBuffer buffer) {
        buffer.writeNbt(this.serializeNetworkData());
    }

    @Override
    default void readSpawnData(PacketBuffer additionalData) {
        this.deserializeNetworkData(additionalData.readNbt());
    }
}
