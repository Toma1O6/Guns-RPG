package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.attribute.IAttributeTarget;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.attribute.*;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.entity.player.PlayerEntity;
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
        int count = (int) Arrays.stream(targets).map(IAttributeTarget::getTargetAttributes).flatMap(Arrays::stream).filter(this::isDisplayable).count();
        ITextComponent[] components = new ITextComponent[desiredLineCount + count];
        SkillUtil.Localizations.prepareEmptyDescriptionLines(getType(), desiredLineCount, components);
        String rawString = String.format("skill.%s.description.", ModUtils.convertToLocalization(getType().getRegistryName()));
        int index = desiredLineCount;
        for (IAttributeTarget target : targets) {
            double value = target.getModifier().getModifierValue();
            for (IAttributeId id : target.getTargetAttributes()) {
                if (!id.hasDisplayTag()) continue;
                IValueFormatter formatter = id.getFormatter();
                int formattedValue = formatter.formatAttributeValue(value);
                String tag = id.getDisplayTag();
                components[index++] = new TranslationTextComponent(rawString + tag, formattedValue);
            }
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
        IAttributeModifier modifier = target.getModifier();
        for (IAttributeId id : target.getTargetAttributes()) {
            IAttribute attribute = provider.getAttribute(id);
            attribute.addModifier(modifier);
        }
    }

    private boolean isDisplayable(IAttributeId id) {
        return id.hasDisplayTag();
    }
}
