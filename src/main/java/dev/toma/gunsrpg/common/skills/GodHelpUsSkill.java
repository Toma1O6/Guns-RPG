package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.skill.IClickableSkill;
import dev.toma.gunsrpg.api.common.skill.ICooldown;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.FlareEntity;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.Interval;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;

public class GodHelpUsSkill extends SimpleSkill implements ICooldown, IClickableSkill, IDescriptionProvider {

    private final DescriptionContainer container;
    private final int maxCooldown;
    private int currentCooldown;

    public GodHelpUsSkill(SkillType<?> type) {
        super(type);
        this.maxCooldown = Interval.hours(1).append(Interval.minutes(30)).valueIn(Interval.Unit.TICK);
        this.container = new DescriptionContainer(type);
        this.container.addProperty("info");
        this.container.addProperty("cooldown", Interval.format(maxCooldown, f -> f.src(Interval.Unit.TICK).out(Interval.Unit.HOUR, Interval.Unit.MINUTE)));
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return container.getLines();
    }

    @Override
    public boolean canApply(PlayerEntity user) {
        return currentCooldown == 0;
    }

    @Override
    public void onUse(PlayerEntity player) {
    }

    @Override
    public boolean canUse() {
        return currentCooldown <= 0;
    }

    @Override
    public void onSkillUsed(ServerPlayerEntity player) {
        currentCooldown = maxCooldown;
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.FLARE_SHOOT, SoundCategory.MASTER, 10.0F, 1.0F);
        player.level.addFreshEntity(new FlareEntity(player.level, player));
        PlayerData.get(player).ifPresent(data -> {
            IAttributeProvider provider = data.getAttributes();
            currentCooldown = (int) (currentCooldown * provider.getAttributeValue(Attribs.AIRDROP_CALL_COOLDOWN));
            data.sync(DataFlags.SKILLS);
        });
    }

    @Override
    public int getCooldown() {
        return currentCooldown;
    }

    @Override
    public void setOnCooldown() {
        currentCooldown = maxCooldown;
    }

    @Override
    public int getMaxCooldown() {
        return maxCooldown;
    }

    @Override
    public void onUpdate(PlayerEntity player) {
        if (currentCooldown > 0) --currentCooldown;
    }

    @Override
    public CompoundNBT saveData() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("cooldown", currentCooldown);
        return nbt;
    }

    @Override
    public void readData(CompoundNBT nbt) {
        currentCooldown = nbt.getInt("cooldown");
    }
}
