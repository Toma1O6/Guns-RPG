package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerDataProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IPlayerData.class)
    public static final Capability<IPlayerData> CAPABILITY = null;
    private final LazyOptional<IPlayerData> instance;

    public PlayerDataProvider(PlayerEntity player) {
        instance = LazyOptional.of(() -> new PlayerData(player));
    }

    public PlayerDataProvider() {
        this(null);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == CAPABILITY ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElse(null), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElse(null), null, nbt);
    }
}
