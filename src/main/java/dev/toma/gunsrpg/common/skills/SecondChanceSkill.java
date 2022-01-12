package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.ICooldown;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.IIntervalProvider;
import dev.toma.gunsrpg.util.Interval;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Supplier;

public class SecondChanceSkill extends BasicSkill implements ICooldown, IDescriptionProvider {

    private final DescriptionContainer container;
    private final int maxCooldown;
    private final int healAmount;
    private final Supplier<EffectInstance> effectSupplier;
    private int cooldown;

    public SecondChanceSkill(SkillType<?> type, IIntervalProvider cooldown, int healAmount, int power) {
        super(type);
        this.maxCooldown = cooldown.getTicks();
        this.healAmount = healAmount;
        this.effectSupplier = () -> new EffectInstance(Effects.REGENERATION, Interval.seconds(10).valueIn(Interval.Unit.TICK), power);
        this.container = new DescriptionContainer(type);
        this.container.addProperty("heal", healAmount);
        this.container.addProperty("cooldown", Interval.format(maxCooldown, f -> f.src(Interval.Unit.TICK).out(Interval.Unit.MINUTE, Interval.Unit.SECOND)));
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return container.getLines();
    }

    @Override
    public void onUpdate(PlayerEntity player) {
        if (cooldown > 0) --cooldown;
    }

    @Override
    public boolean canApply(PlayerEntity user) {
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
    public CompoundNBT saveData() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("cooldown", cooldown);
        return nbt;
    }

    @Override
    public void readData(CompoundNBT nbt) {
        cooldown = nbt.getInt("cooldown");
    }
}
