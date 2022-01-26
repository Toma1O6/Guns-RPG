package dev.toma.gunsrpg.common.item.heal;

import dev.toma.gunsrpg.api.common.attribute.IAttributeTarget;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.attribute.*;
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

        public Builder defineModifiers(IAttributeTarget... targets) {
            return (Builder) onUse(provider -> {
                for (IAttributeTarget target : targets) {
                    IAttributeModifier modifier = target.getModifier();
                    for (IAttributeId id : target.getTargetAttributes()) {
                        provider.getAttribute(id).addModifier(modifier);
                    }
                }
            });
        }

        @Override
        public AttributeAccessHealItem build() {
            return new AttributeAccessHealItem(this);
        }
    }
}
