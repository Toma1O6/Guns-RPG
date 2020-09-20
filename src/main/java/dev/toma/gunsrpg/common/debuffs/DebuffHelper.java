package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.common.GunDamageSourceHack;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
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

    public static void p41_70eff(EntityPlayer player) {
        eff_s(player, () -> new PotionEffect(MobEffects.POISON, 60, 0, false, false));
    }

    public static void p71_85eff(EntityPlayer player) {
        eff_s(player, () -> new PotionEffect(MobEffects.POISON, 60, 1, false, false));
    }

    public static void p86_99eff(EntityPlayer player) {
        eff_m(player, () -> new PotionEffect[] {
                new PotionEffect(MobEffects.POISON, 60, 1, false, false),
                new PotionEffect(MobEffects.NAUSEA, 260, 0, false, false),
                new PotionEffect(MobEffects.BLINDNESS, 260, 0, false, false)
        });
    }

    public static void p100eff(EntityPlayer player) {
        if(!player.world.isRemote && player.world.getWorldTime() % 50L == 0) {
            player.attackEntityFrom(Debuff.POISON_DAMAGE, 15.0F);
        }
    }

    public static int p_progress(PlayerSkills skills) {
        return 140 + skills.poisonResistance;
    }

    public static float p_resist(PlayerSkills skills, DamageSource source) {
        return skills.poisonChance;
    }

    public static float pSpiderCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntitySpider.class, ctx) ? 0.15F : 0.0F;
    }

    public static float pCaveSpiderCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityCaveSpider.class, ctx) ? 0.20F : 0.0F;
    }

    public static float pSkeletonCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntitySkeleton.class, ctx) ? 0.02F : 0.0F;
    }

    public static float pGuardianCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityGuardian.class, ctx) ? 0.05F : 0.0F;
    }

    public static float pElderGuardianCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityElderGuardian.class, ctx) ? 0.08F : 0.0F;
    }

    public static float pSlimeCondition(PlayerSkills skills, DamageContext ctx) {
        DamageSource source = ctx.getSource();
        if(source instanceof EntityDamageSource && source.getTrueSource() instanceof EntitySlime) {
            return 0.03F * ((EntitySlime) source.getTrueSource()).getSlimeSize();
        }
        return 0.0F;
    }

    public static float pStrayCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityStray.class, ctx) ? 0.02F : 0.0F;
    }

    public static float pSilverfishCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntitySilverfish.class, ctx) ? 0.06F : 0.0F;
    }

    public static int i_progress(PlayerSkills skills) {
        return 200 + skills.infectionResistance;
    }

    public static float i_resist(PlayerSkills skills, DamageSource source) {
        return skills.infectionChance;
    }

    public static void i36_60eff(EntityPlayer player) {
        eff_m(player, () -> new PotionEffect[] {
                new PotionEffect(MobEffects.WITHER, 60, 0, false, false),
                new PotionEffect(MobEffects.HUNGER, 60, 4, false, false)
        });
    }

    public static void i61_85eff(EntityPlayer player) {
        eff_m(player, () -> new PotionEffect[] {
                new PotionEffect(MobEffects.WITHER, 60, 1, false, false),
                new PotionEffect(MobEffects.HUNGER, 60, 10, false, false)
        });
    }

    public static void i86_99eff(EntityPlayer player) {
        eff_m(player, () -> new PotionEffect[] {
                new PotionEffect(MobEffects.WITHER, 60, 2, false, false),
                new PotionEffect(MobEffects.HUNGER, 60, 15, false, false)
        });
    }

    public static void i100eff(EntityPlayer player) {
        if(!player.world.isRemote && player.world.getWorldTime() % 50L == 0) {
            player.attackEntityFrom(Debuff.INFECTION_DAMAGE, 15.0F);
        }
    }

    public static float iZombieVillagerCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityZombieVillager.class, ctx) ? 0.06F : 0.0F;
    }

    public static float iEndermanCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityEnderman.class, ctx) ? 0.02F : 0.0F;
    }

    public static float iVindicatorCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityVindicator.class, ctx) ? 0.03F : 0.0F;
    }

    public static float iWitherSkeletonCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityWitherSkeleton.class, ctx) ? 0.07F : 0.0F;
    }

    public static float iHuskCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityHusk.class, ctx) ? 0.07F : 0.0F;
    }

    public static float iZombieCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityZombie.class, ctx) ? 0.06F : 0.0F;
    }

    public static float iPigZombieCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityPigZombie.class, ctx) ? 0.09F : 0.0F;
    }

    public static int f_progress(PlayerSkills skills) {
        return 240 + skills.brokenBoneResistance;
    }

    public static float f_resist(PlayerSkills skills, DamageSource source) {
        return source == DamageSource.FALL || source.isExplosion() ? 0.0F : skills.brokenBoneChance;
    }

    public static void f0_30eff(EntityPlayer player) {
        eff_s(player, () -> new PotionEffect(MobEffects.SLOWNESS, 60, 0, false, false));
    }

    public static void f31_55eff(EntityPlayer player) {
        eff_m(player, () -> new PotionEffect[] {
                new PotionEffect(MobEffects.SLOWNESS, 60, 1, false, false),
                new PotionEffect(MobEffects.MINING_FATIGUE, 60, 0, false, false)
        });
    }

    public static void f56_75eff(EntityPlayer player) {
        eff_m(player, () -> new PotionEffect[] {
                new PotionEffect(MobEffects.SLOWNESS, 60, 2, false, false),
                new PotionEffect(MobEffects.WEAKNESS, 60, 0, false, false),
                new PotionEffect(MobEffects.MINING_FATIGUE, 60, 0, false, false)
        });
    }

    public static void f76_99eff(EntityPlayer player) {
        eff_m(player, () -> new PotionEffect[] {
                new PotionEffect(MobEffects.SLOWNESS, 60, 3, false, false),
                new PotionEffect(MobEffects.WEAKNESS, 60, 0, false, false),
                new PotionEffect(MobEffects.MINING_FATIGUE, 60, 1, false, false)
        });
    }

    public static void f100eff(EntityPlayer player) {
        eff_m(player, () -> new PotionEffect[] {
                new PotionEffect(MobEffects.SLOWNESS, 60, 3, false, false),
                new PotionEffect(MobEffects.WEAKNESS, 60, 1, false, false),
                new PotionEffect(MobEffects.WITHER, 60, 0, false, false),
                new PotionEffect(MobEffects.MINING_FATIGUE, 60, 2, false, false)
        });
    }

    public static float fGenericCondition(PlayerSkills skills, DamageContext ctx) {
        DamageSource source = ctx.getSource();
        return source instanceof EntityDamageSource && !source.isExplosion() && !source.isProjectile() ? 0.01F : 0.0F;
    }

    public static float fFallCondition(PlayerSkills skills, DamageContext ctx) {
        DamageSource source = ctx.getSource();
        if(source == DamageSource.FALL) {
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

    public static void b0_25eff(EntityPlayer player) {
        if(!player.world.isRemote && player.world.getWorldTime() % 80 == 0) {
            player.attackEntityFrom(Debuff.BLEED_DAMAGE, 1.0F);
        }
    }

    public static void b26_50eff(EntityPlayer player) {
        if(!player.world.isRemote && player.world.getWorldTime() % 60 == 0) {
            player.attackEntityFrom(Debuff.BLEED_DAMAGE, 1.0F);
        }
    }

    public static void b51_75eff(EntityPlayer player) {
        if(!player.world.isRemote && player.world.getWorldTime() % 40 == 0) {
            player.attackEntityFrom(Debuff.BLEED_DAMAGE, 1.0F);
        }
    }

    public static void b76_99eff(EntityPlayer player) {
        if(!player.world.isRemote && player.world.getWorldTime() % 20 == 0) {
            player.attackEntityFrom(Debuff.BLEED_DAMAGE, 1.0F);
        }
    }

    public static void b100eff(EntityPlayer player) {
        if(!player.world.isRemote && player.world.getWorldTime() % 10 == 0) {
            player.attackEntityFrom(Debuff.BLEED_DAMAGE, 1.0F);
            player.hurtResistantTime = 9;
        }
    }

    public static float bSpiderCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntitySpider.class, ctx) ? 0.05F : 0.0F;
    }

    public static float bZombieCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityZombie.class, ctx) ? 0.06F : 0.0F;
    }

    public static float bZombieVillagerCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityZombieVillager.class, ctx) ? 0.06F : 0.0F;
    }

    public static float bStrayCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityStray.class, ctx) ? 0.07F : 0.0F;
    }

    public static float bSkeletonCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntitySkeleton.class, ctx) ? 0.07F : 0.0F;
    }

    public static float bEndermanCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityEnderman.class, ctx) ? 0.05F : 0.0F;
    }

    public static float bPigZombieCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityPigZombie.class, ctx) ? 0.09F : 0.0F;
    }

    public static float bWitherSkeletonCondition(PlayerSkills skills, DamageContext ctx) {
        return isEntity(EntityWitherSkeleton.class, ctx) ? 0.08F : 0.0F;
    }

    public static float bExplosionCondition(PlayerSkills skills, DamageContext ctx) {
        return ctx.getSource().isExplosion() ? 0.13F : 0.0F;
    }

    public static float bFallCondition(PlayerSkills skills, DamageContext ctx) {
        return ctx.getSource() == DamageSource.FALL ? 0.05F : 0.0F;
    }

    public static float bGunshotWoundCondition(PlayerSkills skills, DamageContext ctx) {
        return ctx.getSource() instanceof GunDamageSourceHack ? 0.05F : 0.0F;
    }

    protected static boolean isEntity(Class<? extends Entity> eClass, DamageContext ctx) {
        DamageSource source = ctx.getSource();
        return source instanceof EntityDamageSource && source.getTrueSource() != null && source.getTrueSource().getClass().equals(eClass);
    }

    protected static void eff_s(EntityPlayer player, Supplier<PotionEffect> eff) {
        eff_m(player, () -> new PotionEffect[] {eff.get()});
    }

    protected static void eff_m(EntityPlayer player, Supplier<PotionEffect[]> eff) {
        if(!player.world.isRemote && player.world.getWorldTime() % 50L == 0) {
            for(PotionEffect effect : eff.get()) {
                player.addPotionEffect(effect);
            }
        }
    }
}
