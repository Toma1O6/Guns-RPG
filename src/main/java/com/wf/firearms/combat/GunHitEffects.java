package com.wf.firearms.combat;

import com.wf.firearms.debuff.DebuffCause;
import com.wf.firearms.debuff.DebuffService;
import com.wf.firearms.debuff.DebuffType;
import com.wf.firearms.debuff.PlayerDebuffData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/** 枪械命中附加效果：全枪流血梯度 + 枪族专属 debuff。 */
public final class GunHitEffects {
    private static final UUID ARMOR_BREAK_UUID = UUID.fromString("c3d4e5f6-a7b8-9012-cdef-345678901234");

    private GunHitEffects() {}

    public static void applyOnHit(
            LivingEntity target, Player shooter, String weaponKey, FirearmSpec spec, float damageDealt) {
        if (target.level().isClientSide || damageDealt <= 0.01f) {
            return;
        }
        int tier = WeaponBalance.tier(weaponKey);
        applyBleed(target, tier);
        switch (spec.weaponClass()) {
            case SNIPER -> applyArmorBreak(target, tier);
            case SHOTGUN -> applySlow(target, tier);
            case PISTOL -> applySuppression(target, tier);
            case SMG -> applyVenom(target, tier);
            case RIFLE -> applyTrauma(target, tier);
            case HEAVY -> applyHeavyTrauma(target, tier);
            default -> {}
        }
    }

    private static void applyBleed(LivingEntity target, int tier) {
        if (target instanceof ServerPlayer player) {
            int stage = Math.min(3, tier);
            if (player.getRandom().nextFloat() < bleedChance(tier)) {
                int current = com.wf.firearms.debuff.PlayerDebuffData.getStage(player, DebuffType.BLEED);
                DebuffService.applyStage(player, DebuffType.BLEED, Math.max(stage, current), DebuffCause.GUNSHOT);
            }
            return;
        }
        if (target.getRandom().nextFloat() < bleedChance(tier)) {
            int amplifier = Math.max(0, tier - 1);
            int duration = 30 + tier * 25;
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, amplifier, false, true, true));
        }
    }

    private static float bleedChance(int tier) {
        return switch (tier) {
            case 1 -> 0.35f;
            case 2 -> 0.55f;
            default -> 0.75f;
        };
    }

    /** 狙击破甲：临时降低护甲值。 */
    private static void applyArmorBreak(LivingEntity target, int tier) {
        double strip = tier * 3.0;
        AttributeInstance armor = target.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.removeModifier(ARMOR_BREAK_UUID);
            armor.addTransientModifier(
                    new AttributeModifier(ARMOR_BREAK_UUID, "wf_gun_armor_break", -strip, AttributeModifier.Operation.ADDITION));
        }
        AttributeInstance tough = target.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (tough != null) {
            tough.removeModifier(ARMOR_BREAK_UUID);
            tough.addTransientModifier(
                    new AttributeModifier(ARMOR_BREAK_UUID, "wf_gun_armor_break", -tier, AttributeModifier.Operation.ADDITION));
        }
        int duration = 50 + tier * 30;
        target.getPersistentData().putInt("wf_armor_break_until", target.tickCount + duration);
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, Math.min(2, tier - 1), false, true, true));
    }

    private static void applySlow(LivingEntity target, int tier) {
        int duration = 25 + tier * 15;
        target.addEffect(
                new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, Math.min(2, tier - 1), false, true, true));
    }

    /** 手枪：短暂压制（极短减速）。 */
    private static void applySuppression(LivingEntity target, int tier) {
        int duration = 12 + tier * 6;
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 0, false, false, true));
    }

    /** 冲锋枪：中毒倾向（玩家走 Debuff 系统，生物用原版中毒）。 */
    private static void applyVenom(LivingEntity target, int tier) {
        float chance = 0.25f + tier * 0.12f;
        if (target.getRandom().nextFloat() >= chance) {
            return;
        }
        if (target instanceof net.minecraft.server.level.ServerPlayer player) {
            int stage = Math.min(3, Math.max(1, tier - 1));
            int current = PlayerDebuffData.getStage(player, DebuffType.POISON);
            DebuffService.applyStage(player, DebuffType.POISON, Math.max(stage, current), DebuffCause.GUNSHOT);
            return;
        }
        target.addEffect(
                new MobEffectInstance(MobEffects.POISON, 30 + tier * 20, Math.max(0, tier - 2), false, true, true));
    }

    /** 步枪：创伤虚弱。 */
    private static void applyTrauma(LivingEntity target, int tier) {
        target.addEffect(
                new MobEffectInstance(MobEffects.WEAKNESS, 35 + tier * 15, Math.max(0, tier - 2), false, true, true));
    }

    /** 重机枪：强创伤 + 额外流血层。 */
    private static void applyHeavyTrauma(LivingEntity target, int tier) {
        applyTrauma(target, tier);
        if (target.getRandom().nextFloat() < 0.6f) {
            applyBleed(target, Math.min(3, tier + 1));
        }
    }

    /** 清除过期的破甲属性修正（服务端 tick 调用）。 */
    public static void tickArmorBreak(LivingEntity entity) {
        if (entity.level().isClientSide || !entity.getPersistentData().contains("wf_armor_break_until")) {
            return;
        }
        if (entity.tickCount >= entity.getPersistentData().getInt("wf_armor_break_until")) {
            entity.getPersistentData().remove("wf_armor_break_until");
            AttributeInstance armor = entity.getAttribute(Attributes.ARMOR);
            if (armor != null) {
                armor.removeModifier(ARMOR_BREAK_UUID);
            }
            AttributeInstance tough = entity.getAttribute(Attributes.ARMOR_TOUGHNESS);
            if (tough != null) {
                tough.removeModifier(ARMOR_BREAK_UUID);
            }
        }
    }
}
