package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.api.common.data.ISkills;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class ReducedHealthDebuff implements IDebuff {

    private final RespawnDebuffType<?> type;
    private int ticksLeft;

    public ReducedHealthDebuff(RespawnDebuffType<?> type) {
        this.type = type;
        this.ticksLeft = type.getDebuffDuration();
    }

    @Override
    public void tick(PlayerEntity player) {
        ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MAX_HEALTH);
        --ticksLeft;
        if (isActive()) {
            double health = attribute.getBaseValue();
            if (health != 6.0) {
                attribute.setBaseValue(6.0);
            }
        } else {
            if (!player.level.isClientSide) {
                PlayerData.get(player).ifPresent(data -> {
                    ISkills skills = data.getSkills();
                    double health = skills.hasSkill(Skills.WAR_MACHINE) ? 40.0 : 20.0;
                    attribute.setBaseValue(health);
                });
            }
        }
    }

    @Override
    public void heal(int amount, IDebuffs data) {
        throw new UnsupportedOperationException("Attempted to heal unhealable debuff");
    }

    @Override
    public IDebuffType<?> getType() {
        return type;
    }

    @Override
    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("ticksLeft", ticksLeft);
        return nbt;
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        ticksLeft = nbt.getInt("ticksLeft");
    }

    @Override
    public boolean shouldRemove() {
        return !isActive();
    }

    @Override
    public boolean isFrozen(IAttributeProvider attributes) {
        return false;
    }

    private boolean isActive() {
        return ticksLeft > 0;
    }
}
