package dev.toma.gunsrpg.common.item.heal;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class PlayerHealItem extends AbstractHealItem<PlayerEntity> {

    protected PlayerHealItem(Builder builder) {
        super(builder);
    }

    public static HealBuilder<PlayerEntity, PlayerHealItem> define(String name) {
        return new Builder(name);
    }

    public static void onStereoidsUsed(PlayerEntity player) {
        player.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 1200, 0, false, false));
        player.addEffect(new EffectInstance(Effects.JUMP, 1200, 1, false, false));
    }

    public static void onAdrenalineUsed(PlayerEntity player) {
        player.addEffect(new EffectInstance(Effects.REGENERATION, 700, 0, false, false));
        player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 1200, 0, false, false));
    }

    public static void onMorphineUsed(PlayerEntity player) {
        player.heal(14);
        player.addEffect(new EffectInstance(Effects.REGENERATION, 300, 1, false, false));
        player.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 900, 1, false, false));
        player.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 1200, 0, false, false));
    }

    @Override
    public PlayerEntity getTargetObject(World world, PlayerEntity user, IPlayerData data) {
        return user;
    }

    protected static class Builder extends HealBuilder<PlayerEntity, PlayerHealItem> {

        protected Builder(String name) {
            super(name);
        }

        @Override
        public PlayerHealItem build() {
            return new PlayerHealItem(this);
        }
    }
}
