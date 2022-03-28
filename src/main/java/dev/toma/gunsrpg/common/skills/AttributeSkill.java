package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.attribute.*;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AttributeSkill extends BasicSkill implements IDescriptionProvider {

    private final IAttributeTarget[] targets;

    public AttributeSkill(SkillType<? extends AttributeSkill> type, IAttributeTarget... targets) {
        super(type);
        this.targets = targets;
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        List<IAttributeTarget> displayableAttributes = Arrays.stream(targets).filter(this::isDisplayable).collect(Collectors.toList());
        int count = displayableAttributes.size();
        ITextComponent[] components = new ITextComponent[desiredLineCount + count];
        SkillUtil.Localizations.prepareEmptyDescriptionLines(getType(), desiredLineCount, components);
        String rawString = String.format("skill.%s.description.", ModUtils.convertToLocalization(getType().getRegistryName()));
        int index = desiredLineCount;
        for (IAttributeTarget target : displayableAttributes) {
            IDisplayableModifier displayableModifier = (IDisplayableModifier) target.getModifier();
            double modifierValue = target.getModifier().getModifierValue();
            int formatterValue = displayableModifier.getFormatter().formatAttributeValue(modifierValue);
            String tag = displayableModifier.getTagId();
            components[index++] = new TranslationTextComponent(rawString + tag, formatterValue);
        }
        return components;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onPurchase(PlayerEntity player) {
        PlayerData.get(player).ifPresent(data -> {
            ISkillProvider skillProvider = data.getSkillProvider();
            AttributeSkill skill = ModUtils.either(SkillUtil.getTopHierarchySkill((SkillType<AttributeSkill>) this.getType(), skillProvider), this);
            IAttributeProvider provider = data.getAttributes();
            for (IAttributeTarget target : skill.targets) {
                applyTarget(target, provider);
            }
        });
    }

    @Override
    public void onDeactivate(PlayerEntity player) {
        PlayerData.get(player).ifPresent(data -> {
            IAttributeProvider provider = data.getAttributes();
            for (IAttributeTarget target : targets) {
                IAttributeId attributeId = target.getTargetAttribute();
                IAttributeModifier modifier = target.getModifier();
                provider.getAttribute(attributeId).removeModifier(modifier);
            }
        });
    }

    private void applyTarget(IAttributeTarget target, IAttributeProvider provider) {
        IAttributeModifier modifier = target.getModifier();
        IAttributeId attributeId = target.getTargetAttribute();
        IAttribute attribute = provider.getAttribute(attributeId);
        attribute.addModifier(modifier);
    }

    private boolean isDisplayable(IAttributeTarget target) {
        return target.getModifier() instanceof IDisplayableModifier;
    }
}
