package dev.toma.gunsrpg.world.cap;

import dev.toma.gunsrpg.api.common.data.IWorldData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class WorldDataProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IWorldData.class)
    public static Capability<IWorldData> CAP = null;
    private final LazyOptional<IWorldData> instance;

    public WorldDataProvider(World world) {
        instance = LazyOptional.of(() -> new WorldData(world));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) CAP.getStorage().writeNBT(CAP, instance.orElse(null), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CAP.getStorage().readNBT(CAP, instance.orElse(null), null, nbt);
    }
}
