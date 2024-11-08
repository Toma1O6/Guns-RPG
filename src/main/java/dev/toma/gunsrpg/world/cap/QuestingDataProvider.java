package dev.toma.gunsrpg.world.cap;

import dev.toma.gunsrpg.api.common.data.IQuestingData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class QuestingDataProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IQuestingData.class)
    public static final Capability<IQuestingData> QUESTING_DATA_CAPABILITY = null;
    private final LazyOptional<IQuestingData> instance;

    public QuestingDataProvider(World world) {
        this.instance = LazyOptional.of(() -> new QuestingData(world));
    }

    public static LazyOptional<IQuestingData> getData(World world) {
        return world.getCapability(QUESTING_DATA_CAPABILITY);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == QUESTING_DATA_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) QUESTING_DATA_CAPABILITY.getStorage().writeNBT(QUESTING_DATA_CAPABILITY, instance.orElse(null), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        QUESTING_DATA_CAPABILITY.getStorage().readNBT(QUESTING_DATA_CAPABILITY, instance.orElse(null), null, nbt);
    }
}
