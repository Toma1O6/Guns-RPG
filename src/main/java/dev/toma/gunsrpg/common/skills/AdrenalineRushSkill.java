package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.attribute.IAttribute;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.attribute.IValueFormatter;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.api.common.skill.ITickableSkill;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.AttributeModifier;
import dev.toma.gunsrpg.common.attribute.AttributeOps;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

import java.util.function.BiConsumer;

public class AdrenalineRushSkill extends SimpleSkill implements ITickableSkill, IDescriptionProvider {

    private static final float[] MODIFIERS = new float[]{0.15F, 0.3F, 0.5F};
    private static final int HEALTH_LIMIT = 8;

    private final DescriptionContainer container;
    private final IAttributeModifier cooldownModifier;
    private final IAttributeModifier reloadModifier;
    private boolean isApplied;

    public AdrenalineRushSkill(SkillType<?> type, int level, float reloadMultiplier) {
        super(type);
        this.cooldownModifier = new AttributeModifier("EC2FBF66-9F72-417B-AF43-91EA879FCC1A", AttributeOps.MULB, MODIFIERS[level]);
        this.reloadModifier = new AttributeModifier("808184F6-B4BC-4981-9A96-B28E3AF945B6", AttributeOps.MUL, reloadMultiplier);
        this.container = new DescriptionContainer(type);
        this.container.addProperty("predicate", HEALTH_LIMIT);
        this.container.addProperty("attack", IValueFormatter.PERCENT.formatAttributeValue(cooldownModifier.getModifierValue()));
        this.container.addProperty("reload", IValueFormatter.INV_PERCENT.formatAttributeValue(reloadModifier.getModifierValue()));
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return container.getLines();
    }

    @Override
    public void onUpdate(PlayerEntity player) {
        boolean wasApplied = isApplied;
        isApplied = canApply(player);
        if (wasApplied != isApplied) {
            BiConsumer<IAttribute, IAttributeModifier> operation = isApplied ? IAttribute::addModifier : IAttribute::removeModifier;
            PlayerData.get(player).ifPresent(data -> {
                IAttributeProvider provider = data.getAttributes();
                IAttribute attackSpeed = provider.getAttribute(Attribs.MELEE_COOLDOWN);
                IAttribute reloadSpeed = provider.getAttribute(Attribs.RELOAD_SPEED);
                operation.accept(attackSpeed, cooldownModifier);
                operation.accept(reloadSpeed, reloadModifier);
            });
        }
    }

    @Override
    public boolean canApply(PlayerEntity user) {
        return user.getHealth() <= HEALTH_LIMIT;
    }
}
