package dev.toma.gunsrpg.world.cap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class WorldCapProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(WorldDataCap.class)
    public static Capability<WorldDataCap> CAP = null;
    private final LazyOptional<WorldDataCap> instance;

    public WorldCapProvider(World world) {
        instance = LazyOptional.of(() -> new WorldDataFactory(world));
    }

    public WorldCapProvider() {
        this(null);
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
