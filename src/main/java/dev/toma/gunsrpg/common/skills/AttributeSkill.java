package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.attribute.IAttribute;
import dev.toma.gunsrpg.common.attribute.IAttributeId;
import dev.toma.gunsrpg.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class AttributeSkill extends BasicSkill {

    private final IAttributeTarget[] targets;

    public AttributeSkill(SkillType<? extends AttributeSkill> type, IAttributeTarget... targets) {
        super(type);
        this.targets = targets;
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
