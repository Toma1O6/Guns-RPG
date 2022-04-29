package dev.toma.gunsrpg.common.item.heal;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.util.SkillUtil;
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

    public static void onSteroidsUsed(PlayerEntity player) {
        float value = PlayerData.getUnsafe(player).getAttributes().getAttribute(Attribs.STEROIDS_EFFECT).floatValue();
        int length = (int) (1200 * value);
        player.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, length, 0, false, false));
        player.addEffect(new EffectInstance(Effects.JUMP, length, 1, false, false));
    }

    public static void onAdrenalineUsed(PlayerEntity player) {
        float value = PlayerData.getUnsafe(player).getAttributes().getAttribute(Attribs.ADRENALINE_EFFECT).floatValue();
        player.addEffect(new EffectInstance(Effects.REGENERATION, (int) (700 * value), 0, false, false));
        player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, (int) (1200 * value), 0, false, false));
    }

    public static void onMorphineUsed(PlayerEntity player) {
        SkillUtil.heal(player, 14);
        float value = PlayerData.getUnsafe(player).getAttributes().getAttribute(Attribs.MORPHINE_EFFECT).floatValue();
        player.addEffect(new EffectInstance(Effects.REGENERATION, (int) (300 * value), 1, false, false));
        player.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, (int) (900 * value), 1, false, false));
        player.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, (int) (1200 * value), 0, false, false));
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
