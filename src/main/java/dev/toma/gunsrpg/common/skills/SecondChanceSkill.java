package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.ICooldown;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;

import java.util.function.Supplier;

public class SecondChanceSkill extends BasicSkill implements ICooldown {

    private final int maxCooldown;
    private final int healAmount;
    private final Supplier<EffectInstance> effectSupplier;
    private int cooldown;

    public SecondChanceSkill(SkillType<?> type, int maxCooldown, int healAmount, Supplier<EffectInstance> effectSupplier) {
        super(type);
        this.maxCooldown = maxCooldown;
        this.healAmount = healAmount;
        this.effectSupplier = effectSupplier;
    }

    @Override
    public void onUpdate(PlayerEntity player) {
        if (cooldown > 0) --cooldown;
    }

    @Override
    public boolean apply(PlayerEntity user) {
        return getCooldown() == 0;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public void setOnCooldown() {
        this.cooldown = getMaxCooldown();
    }

    @Override
    public int getMaxCooldown() {
        return maxCooldown;
    }

    @Override
    public void onUse(PlayerEntity player) {
        player.setHealth(this.healAmount);
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SECOND_CHANCE_USE, SoundCategory.MASTER, 1.0F, 1.0F);
        player.addEffect(effectSupplier.get());
    }

    @Override
    public void writeExtra(CompoundNBT nbt) {
        nbt.putInt("cooldown", cooldown);
    }

    @Override
    public void readExtra(CompoundNBT nbt) {
        cooldown = nbt.getInt("cooldown");
    }
}
