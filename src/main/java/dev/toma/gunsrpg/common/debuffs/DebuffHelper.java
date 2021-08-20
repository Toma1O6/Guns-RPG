package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.common.init.GunDamageSourceSpecial;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

import java.util.function.Supplier;

/**
 * Ugly class, nothing to see really.
 * <p>Represents utility class for <i>internal</i> stuff (registry)
 */
public class DebuffHelper {

    public static <T> void none(T t) {
    }

    public static void p41_70eff(PlayerEntity player) {
        eff_s(player, () -> new EffectInstance(Effects.POISON, 60, 0, false, false));
    }

    public static void p71_85eff(PlayerEntity player) {
        eff_s(player, () -> new EffectInstance(Effects.POISON, 60, 1, false, false));
    }

    public static void p86_99eff(PlayerEntity player) {
        eff_m(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.POISON, 60, 1, false, false),
                new EffectInstance(Effects.CONFUSION, 260, 0, false, false),
                new EffectInstance(Effects.BLINDNESS, 260, 0, false, false)
        });
    }

    public static void p100eff(PlayerEntity player) {
        if (!player.level.isClientSide && player.level.getGameTime() % 50L == 0) {
            player.hurt(Debuff.POISON_DAMAGE, 15.0F);
        }
    }

    public static int p_progress(PlayerSkills skills) {
        return 140 + skills.poisonResistance;
    }

    public static float p_resist(PlayerSkills skills, DamageSource source) {
        return skills.poisonChance;
    }

    public static float pSpiderCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.SPIDER, ctx) ? 0.15F : 0.0F;
    }

    public static float pCaveSpiderCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.CAVE_SPIDER, ctx) ? 0.20F : 0.0F;
    }

    public static float pSkeletonCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.SKELETON, ctx) ? 0.02F : 0.0F;
    }

    public static float pGuardianCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.GUARDIAN, ctx) ? 0.05F : 0.0F;
    }

    public static float pElderGuardianCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.ELDER_GUARDIAN, ctx) ? 0.08F : 0.0F;
    }

    public static float pSlimeCondition(PlayerSkills skills, DamageContext ctx) {
        DamageSource source = ctx.getSource();
        if (source instanceof EntityDamageSource && source.getDirectEntity() instanceof SlimeEntity) {
            return 0.03F * ((SlimeEntity) source.getDirectEntity()).getSize();
        }
        return 0.0F;
    }

    public static float pStrayCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.STRAY, ctx) ? 0.02F : 0.0F;
    }

    public static float pSilverfishCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.SILVERFISH, ctx) ? 0.06F : 0.0F;
    }

    public static int i_progress(PlayerSkills skills) {
        return 200 + skills.infectionResistance;
    }

    public static float i_resist(PlayerSkills skills, DamageSource source) {
        return skills.infectionChance;
    }

    public static void i36_60eff(PlayerEntity player) {
        eff_m(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.WITHER, 60, 0, false, false),
                new EffectInstance(Effects.HUNGER, 60, 4, false, false)
        });
    }

    public static void i61_85eff(PlayerEntity player) {
        eff_m(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.WITHER, 60, 1, false, false),
                new EffectInstance(Effects.HUNGER, 60, 10, false, false)
        });
    }

    public static void i86_99eff(PlayerEntity player) {
        eff_m(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.WITHER, 60, 2, false, false),
                new EffectInstance(Effects.HUNGER, 60, 15, false, false)
        });
    }

    public static void i100eff(PlayerEntity player) {
        if (!player.level.isClientSide && player.level.getGameTime() % 50L == 0) {
            player.hurt(Debuff.INFECTION_DAMAGE, 15.0F);
        }
    }

    public static float iZombieVillagerCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.ZOMBIE_VILLAGER, ctx) ? 0.06F : 0.0F;
    }

    public static float iEndermanCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.ENDERMAN, ctx) ? 0.02F : 0.0F;
    }

    public static float iVindicatorCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.VINDICATOR, ctx) ? 0.03F : 0.0F;
    }

    public static float iWitherSkeletonCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.WITHER_SKELETON, ctx) ? 0.07F : 0.0F;
    }

    public static float iHuskCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.HUSK, ctx) ? 0.07F : 0.0F;
    }

    public static float iZombieCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.ZOMBIE, ctx) ? 0.06F : 0.0F;
    }

    public static float iPigZombieCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.ZOMBIFIED_PIGLIN, ctx) ? 0.09F : 0.0F;
    }

    public static int f_progress(PlayerSkills skills) {
        return 240 + skills.brokenBoneResistance;
    }

    public static float f_resist(PlayerSkills skills, DamageSource source) {
        return source == DamageSource.FALL || source.isExplosion() ? 0.0F : skills.brokenBoneChance;
    }

    public static void f0_30eff(PlayerEntity player) {
        eff_s(player, () -> new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 0, false, false));
    }

    public static void f31_55eff(PlayerEntity player) {
        eff_m(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 1, false, false),
                new EffectInstance(Effects.DIG_SLOWDOWN, 60, 0, false, false)
        });
    }

    public static void f56_75eff(PlayerEntity player) {
        eff_m(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 2, false, false),
                new EffectInstance(Effects.WEAKNESS, 60, 0, false, false),
                new EffectInstance(Effects.DIG_SLOWDOWN, 60, 0, false, false)
        });
    }

    public static void f76_99eff(PlayerEntity player) {
        eff_m(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 3, false, false),
                new EffectInstance(Effects.WEAKNESS, 60, 0, false, false),
                new EffectInstance(Effects.DIG_SLOWDOWN, 60, 1, false, false)
        });
    }

    public static void f100eff(PlayerEntity player) {
        eff_m(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 3, false, false),
                new EffectInstance(Effects.WEAKNESS, 60, 1, false, false),
                new EffectInstance(Effects.WITHER, 60, 0, false, false),
                new EffectInstance(Effects.DIG_SLOWDOWN, 60, 2, false, false)
        });
    }

    public static float fGenericCondition(PlayerSkills skills, DamageContext ctx) {
        DamageSource source = ctx.getSource();
        return source instanceof EntityDamageSource && !source.isExplosion() && !source.isProjectile() ? 0.01F : 0.0F;
    }

    public static float fFallCondition(PlayerSkills skills, DamageContext ctx) {
        DamageSource source = ctx.getSource();
        if (source == DamageSource.FALL) {
            float base = 6.0F;
            float actual = ctx.getAmount();
            float computed = 1.0F + ((actual - base) / base);
            return computed * (1.0F - skills.acrobaticsFallResistance);
        }
        return 0.0F;
    }

    public static float fExplosionCondition(PlayerSkills skills, DamageContext ctx) {
        return ctx.getSource().isExplosion() ? 0.2F * (1.0F - skills.acrobaticsExplosionResistance) : 0.0F;
    }

    public static int b_progress(PlayerSkills skills) {
        return 120 + skills.bleedResistance;
    }

    public static float b_resist(PlayerSkills skills, DamageSource source) {
        return skills.bleedChance;
    }

    public static void b0_25eff(PlayerEntity player) {
        if (!player.level.isClientSide && player.level.getGameTime() % 80 == 0) {
            player.hurt(Debuff.BLEED_DAMAGE, 1.0F);
        }
    }

    public static void b26_50eff(PlayerEntity player) {
        if (!player.level.isClientSide && player.level.getGameTime() % 60 == 0) {
            player.hurt(Debuff.BLEED_DAMAGE, 1.0F);
        }
    }

    public static void b51_75eff(PlayerEntity player) {
        if (!player.level.isClientSide && player.level.getGameTime() % 40 == 0) {
            player.hurt(Debuff.BLEED_DAMAGE, 1.0F);
        }
    }

    public static void b76_99eff(PlayerEntity player) {
        if (!player.level.isClientSide && player.level.getGameTime() % 20 == 0) {
            player.hurt(Debuff.BLEED_DAMAGE, 1.0F);
        }
    }

    public static void b100eff(PlayerEntity player) {
        if (!player.level.isClientSide && player.level.getGameTime() % 10 == 0) {
            player.hurt(Debuff.BLEED_DAMAGE, 1.0F);
            player.hurtTime = 9;
        }
    }

    public static float bSpiderCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.SPIDER, ctx) ? 0.05F : 0.0F;
    }

    public static float bZombieCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.ZOMBIE, ctx) ? 0.06F : 0.0F;
    }

    public static float bZombieVillagerCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.ZOMBIE_VILLAGER, ctx) ? 0.06F : 0.0F;
    }

    public static float bStrayCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.STRAY, ctx) ? 0.07F : 0.0F;
    }

    public static float bSkeletonCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.SKELETON, ctx) ? 0.07F : 0.0F;
    }

    public static float bEndermanCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.ENDERMAN, ctx) ? 0.05F : 0.0F;
    }

    public static float bPigZombieCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.ZOMBIFIED_PIGLIN, ctx) ? 0.09F : 0.0F;
    }

    public static float bWitherSkeletonCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityType.WITHER_SKELETON, ctx) ? 0.08F : 0.0F;
    }

    public static float bExplosionCondition(PlayerSkills skills, DamageContext ctx) {
        return ctx.getSource().isExplosion() ? 0.13F : 0.0F;
    }

    public static float bFallCondition(PlayerSkills skills, DamageContext ctx) {
        return ctx.getSource() == DamageSource.FALL ? 0.05F : 0.0F;
    }

    public static float bGunshotWoundCondition(PlayerSkills skills, DamageContext ctx) {
        return ctx.getSource() instanceof GunDamageSourceSpecial ? 0.05F : 0.0F;
    }

    protected static boolean isEntity(EntityType<?> type, DamageContext ctx) {
        DamageSource source = ctx.getSource();
        return source instanceof EntityDamageSource && source.getDirectEntity() != null && source.getDirectEntity().getType() == type;
    }

    protected static void eff_s(PlayerEntity player, Supplier<EffectInstance> eff) {
        eff_m(player, () -> new EffectInstance[]{eff.get()});
    }

    protected static void eff_m(PlayerEntity player, Supplier<EffectInstance[]> eff) {
        if (!player.level.isClientSide && player.level.getGameTime() % 50L == 0) {
            for (EffectInstance effect : eff.get()) {
                player.addEffect(effect);
            }
        }
    }
}
