package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public interface IDebuff {

    void tick(PlayerEntity player);

    void heal(int amount, IDebuffs data);

    boolean shouldRemove();

    boolean isFrozen(IAttributeProvider attributes);

    CompoundNBT toNbt();

    void fromNbt(CompoundNBT nbt);

    IDebuffType<?> getType();
}
