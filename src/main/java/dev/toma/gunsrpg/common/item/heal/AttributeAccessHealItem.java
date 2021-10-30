package dev.toma.gunsrpg.common.item.heal;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.attribute.AttributeModifierFactory;
import dev.toma.gunsrpg.common.attribute.AttributeOps;
import dev.toma.gunsrpg.common.attribute.IAttributeId;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.UUID;

public class AttributeAccessHealItem extends AbstractHealItem<IAttributeProvider> {

    protected AttributeAccessHealItem(Builder builder) {
        super(builder);
    }

    public static Builder define(String name) {
        return new Builder(name);
    }

    @Override
    public IAttributeProvider getTargetObject(World world, PlayerEntity user, IPlayerData data) {
        return data.getAttributes();
    }

    public static class Builder extends HealBuilder<IAttributeProvider, AttributeAccessHealItem> {

        protected Builder(String name) {
            super(name);
        }

        public Builder modifyAttributes(IAttributeId blockingAttribute, UUID blockingUid, IAttributeId delayAttribute, UUID delayUid) {
            return (Builder) onUse(provider -> {
                provider.getAttribute(blockingAttribute).addModifier(AttributeModifierFactory.temporary(blockingUid, AttributeOps.SUM, 1, 1200));
                provider.getAttribute(delayAttribute).addModifier(AttributeModifierFactory.temporary(delayUid, AttributeOps.MUL, 0.7, 1200));
            });
        }

        @Override
        public AttributeAccessHealItem build() {
            return new AttributeAccessHealItem(this);
        }
    }
}
