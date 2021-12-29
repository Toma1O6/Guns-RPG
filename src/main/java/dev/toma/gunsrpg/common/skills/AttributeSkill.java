package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.attribute.*;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;

public class AttributeSkill extends BasicSkill implements IDescriptionProvider {

    private final IAttributeTarget[] targets;

    public AttributeSkill(SkillType<? extends AttributeSkill> type, IAttributeTarget... targets) {
        super(type);
        this.targets = targets;
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        IAttributeTarget[] displayTargets = Arrays.stream(targets).filter(this::isDisplayable).toArray(IAttributeTarget[]::new);
        ITextComponent[] components = new ITextComponent[desiredLineCount + displayTargets.length];
        SkillUtil.Localizations.prepareEmptyDescriptionLines(getType(), desiredLineCount, components);
        String rawString = String.format("skill.%s.description.", ModUtils.convertToLocalization(getType().getRegistryName()));
        for (int i = desiredLineCount; i < components.length; i++) {
            IAttributeTarget target = targets[i - desiredLineCount];
            IAttributeId attributeId = target.getAttributeId();
            double value = Arrays.stream(target.getModifiers()).mapToDouble(IAttributeModifier::getModifierValue).sum();
            IValueFormatter formatter = attributeId.getFormatter();
            int formattedValue = formatter.formatAttributeValue(value);
            String tag = attributeId.getDisplayTag();
            components[i] = new TranslationTextComponent(rawString + tag, formattedValue);
        }
        return components;
    }

    @Override
    public void onPurchase(PlayerEntity player) {
        PlayerData.get(player).ifPresent(data -> {
            IAttributeProvider provider = data.getAttributes();
            for (IAttributeTarget target : targets) {
                applyTarget(target, provider);
            }
        });
    }

    private void applyTarget(IAttributeTarget target, IAttributeProvider provider) {
        IAttributeId id = target.getAttributeId();
        IAttributeModifier[] modifiers = target.getModifiers();
        IAttribute attribute = provider.getAttribute(id);
        for (IAttributeModifier modifier : modifiers) {
            attribute.addModifier(modifier);
        }
    }

    private boolean isDisplayable(IAttributeTarget target) {
        return target.getAttributeId().hasDisplayTag();
    }

    public static class SimpleAttributeTarget implements IAttributeTarget {

        private final IAttributeId attributeId;
        private final IAttributeModifier[] modifiers;

        public SimpleAttributeTarget(IAttributeId attributeId, IAttributeModifier... modifiers) {
            this.attributeId = attributeId;
            this.modifiers = modifiers;
        }

        @Override
        public IAttributeId getAttributeId() {
            return attributeId;
        }

        @Override
        public IAttributeModifier[] getModifiers() {
            return modifiers;
        }
    }

    public interface IAttributeTarget {

        IAttributeId getAttributeId();

        IAttributeModifier[] getModifiers();

        static IAttributeTarget of(IAttributeId id, IAttributeModifier... modifiers) {
            return new SimpleAttributeTarget(id, modifiers);
        }
    }
}
