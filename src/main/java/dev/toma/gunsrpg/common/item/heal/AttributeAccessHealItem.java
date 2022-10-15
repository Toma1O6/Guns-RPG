package dev.toma.gunsrpg.common.item.heal;

import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.attribute.IAttributeTarget;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.debuffs.IDebuffType;
import dev.toma.gunsrpg.common.debuffs.StagedDebuffType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class AttributeAccessHealItem extends AbstractHealItem<IPlayerData> {

    protected AttributeAccessHealItem(Builder builder) {
        super(builder);
    }

    public static Builder define(String name) {
        return new Builder(name);
    }

    @Override
    public IPlayerData getTargetObject(World world, PlayerEntity user, IPlayerData data) {
        return data;
    }

    public static class Builder extends HealBuilder<IPlayerData, AttributeAccessHealItem> {

        protected Builder(String name) {
            super(name);
        }

        public Builder defineModifiers(Function<IAttributeProvider, IAttributeTarget[]> function, @Nullable Supplier<? extends StagedDebuffType<?>> targetDebuff) {
            return (Builder) onUse(data -> {
                IAttributeProvider provider = data.getAttributes();
                IAttributeTarget[] targets = function.apply(provider);
                for (IAttributeTarget target : targets) {
                    IAttributeModifier modifier = target.getModifier();
                    IAttributeId id = target.getTargetAttribute();
                    provider.getAttribute(id).addModifier(modifier);
                }
                if (targetDebuff != null) {
                    StagedDebuffType<?> stagedDebuffType = targetDebuff.get();
                    IDebuffs debuffs = data.getDebuffControl();
                    debuffs.trigger(IDebuffType.TriggerFlags.HEAL, IDebuffContext.of(DamageSource.GENERIC, null, data, 0.0F), stagedDebuffType);
                }
            });
        }

        @Override
        public AttributeAccessHealItem build() {
            return new AttributeAccessHealItem(this);
        }
    }
}
