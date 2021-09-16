package dev.toma.gunsrpg.common.debuffs;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.BooleanSupplier;

public abstract class DebuffType<D extends IDebuff> extends ForgeRegistryEntry<DebuffType<?>> implements IDebuffType<D> {

    private final IFactory<D> factory;
    private final BooleanSupplier isBlacklisted;

    public DebuffType(IDebuffBuilder<D> builder) {
        this.factory = builder.getFactory();
        this.isBlacklisted = builder.disabledStatusSupplier();
    }

    @Override
    public D createRaw() {
        return factory.createWithType(this);
    }

    @Override
    public boolean isDisabled() {
        return isBlacklisted.getAsBoolean();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof DebuffType<?>) {
            DebuffType<?> other = (DebuffType<?>) obj;
            return other.getRegistryName().equals(this.getRegistryName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getRegistryName().hashCode();
    }

    public interface IFactory<D extends IDebuff> {
        D createWithType(IDebuffType<D> type);
    }
}
