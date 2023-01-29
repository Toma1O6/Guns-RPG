package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class DummyDebuff implements IStagedDebuff {

    private final DummyDebuffType<?> type;
    private boolean forRemoval;

    public DummyDebuff(IDebuffType<?> type) {
        this.type = (DummyDebuffType<?>) type;
    }

    @Override
    public void tick(PlayerEntity player) {
        DataDrivenDebuffType<?> link = this.type.getLinkedType();
        PlayerData.get(player).ifPresent(data -> {
            IAttributeProvider provider = data.getAttributes();
            if (!link.isDisabledByAttributes(provider)) {
                this.forRemoval = true;
            }
        });
    }

    @Override
    public boolean canSpread() {
        return false;
    }

    @Override
    public int getCurrentProgress() {
        return 0;
    }

    @Override
    public int ticksSinceAdded() {
        return 123456;
    }

    @Override
    public int ticksSinceProgressed() {
        return 0;
    }

    @Override
    public int ticksSinceHealed() {
        return 0;
    }

    @Override
    public float getBlockingProgress(IAttributeProvider provider) {
        return DataDrivenDebuffType.getBuffedProgress(provider, this.type.getLinkedType().getBlockingAttribute());
    }

    @Override
    public void heal(int amount, IDebuffs data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean shouldRemove() {
        return forRemoval;
    }

    @Override
    public boolean isFrozen(IAttributeProvider attributes) {
        return true;
    }

    @Override
    public CompoundNBT toNbt() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putBoolean("remove", forRemoval);
        return compoundNBT;
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        this.forRemoval = nbt.getBoolean("remove");
    }

    @Override
    public IDebuffType<?> getType() {
        return type;
    }
}
