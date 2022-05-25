package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.skill.IClickableSkill;
import dev.toma.gunsrpg.api.common.skill.ICooldown;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.IIntervalProvider;
import dev.toma.gunsrpg.util.Interval;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;

public class LikeACatSkill extends SimpleSkill implements ICooldown, IClickableSkill, IDescriptionProvider {

    private final DescriptionContainer container;
    private final int effectLength;
    private final int totalCooldown;
    private int effectLeft;
    private int cooldown;

    public LikeACatSkill(SkillType<?> type, IIntervalProvider totalCooldown, IIntervalProvider effectLength) {
        super(type);
        this.effectLength = effectLength.getTicks();
        this.totalCooldown = totalCooldown.getTicks() + this.effectLength;
        this.container = new DescriptionContainer(type);
        this.container.addProperty("effect", Interval.format(this.effectLength, f -> f.src(Interval.Unit.TICK).out(Interval.Unit.MINUTE, Interval.Unit.SECOND)));
        this.container.addProperty("cooldown", Interval.format(this.totalCooldown, f -> f.src(Interval.Unit.TICK).out(Interval.Unit.MINUTE, Interval.Unit.SECOND)));
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return container.getLines();
    }

    @Override
    public boolean canApply(PlayerEntity user) {
        return effectLeft == 0 && cooldown == 0;
    }

    @Override
    public void onUpdate(PlayerEntity player) {
        if (effectLeft > 0) {
            --effectLeft;
        }
        if (cooldown > 0) {
            --cooldown;
        }
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public int getMaxCooldown() {
        return totalCooldown;
    }

    @Override
    public void setOnCooldown() {
        this.cooldown = totalCooldown;
    }

    @Override
    public void onUse(PlayerEntity player) {

    }

    @Override
    public boolean canUse() {
        return cooldown == 0;
    }

    @Override
    public void onSkillUsed(ServerPlayerEntity player) {
        effectLeft = effectLength;
        setOnCooldown();
        IPlayerData data = PlayerData.getUnsafe(player);
        IAttributeProvider provider = data.getAttributes();
        int effectDuration = (int) (effectLength * provider.getAttributeValue(Attribs.LIKE_A_CAT_EFFECT));
        player.addEffect(new EffectInstance(Effects.NIGHT_VISION, effectDuration, 0, false, false));
        data.sync(DataFlags.SKILLS);
    }

    @Override
    public CompoundNBT saveData() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("effect", effectLeft);
        nbt.putInt("cooldown", cooldown);
        return nbt;
    }

    @Override
    public void readData(CompoundNBT nbt) {
        effectLeft = nbt.getInt("effect");
        cooldown = nbt.getInt("cooldown");
    }
}
