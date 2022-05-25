package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.skill.IClickableSkill;
import dev.toma.gunsrpg.api.common.skill.ICooldown;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.IIntervalProvider;
import dev.toma.gunsrpg.util.Interval;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;

public class IronBuddySkill extends SimpleSkill implements ICooldown, IClickableSkill, IDescriptionProvider {

    private final DescriptionContainer container;
    private final float addedHealth;
    private final int totalCooldown;
    private int cooldown;

    public IronBuddySkill(SkillType<?> type, IIntervalProvider provider) {
        this(type, provider, 0.0F);
    }

    public IronBuddySkill(SkillType<?> type, IIntervalProvider provider, float extraHealth) {
        super(type);
        this.totalCooldown = provider.getTicks();
        this.addedHealth = extraHealth;
        this.container = new DescriptionContainer(type);
        container.addProperty("info", Math.round(100.0F + extraHealth));
        container.addProperty("cooldown", Interval.format(totalCooldown, initial -> initial.src(Interval.Unit.TICK).out(Interval.Unit.MINUTE)));
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return container.getLines();
    }

    @Override
    public int getMaxCooldown() {
        return totalCooldown;
    }

    @Override
    public int getCooldown() {
        return cooldown;
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
        return cooldown <= 0;
    }

    @Override
    public void onSkillUsed(ServerPlayerEntity player) {
        setOnCooldown();
        IronGolemEntity ironGolem = new IronGolemEntity(EntityType.IRON_GOLEM, player.level);
        Vector3d pos = player.position();
        ironGolem.setPos(pos.x, pos.y, pos.z);
        ironGolem.setPlayerCreated(true);
        if (addedHealth > 0) {
            ModifiableAttributeInstance attributeInstance = ironGolem.getAttribute(Attributes.MAX_HEALTH);
            float newValue = (float) (attributeInstance.getBaseValue() + addedHealth);
            attributeInstance.setBaseValue(newValue);
            ironGolem.setHealth(newValue);
        }
        player.level.addFreshEntity(ironGolem);
        PlayerData.get(player).ifPresent(data -> {
            IAttributeProvider provider = data.getAttributes();
            cooldown = (int) (cooldown * provider.getAttributeValue(Attribs.IRON_BUDDY_COOLDOWN));
            data.sync(DataFlags.SKILLS);
        });
    }

    @Override
    public void onUpdate(PlayerEntity player) {
        if (cooldown > 0) {
            --cooldown;
        }
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
