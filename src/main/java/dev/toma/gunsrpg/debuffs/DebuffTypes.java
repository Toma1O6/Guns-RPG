package dev.toma.gunsrpg.debuffs;

import dev.toma.gunsrpg.debuffs.effect.BrokenBoneDebuff;
import dev.toma.gunsrpg.debuffs.effect.InfectionDebuff;
import dev.toma.gunsrpg.debuffs.effect.PoisonDebuff;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DebuffTypes {

    public static final DebuffType<PoisonDebuff> POISON = DebuffType.TypeBuilder.create(PoisonDebuff::new)
            .name("poison")
            .cap(140)
            .addHitData()
            .condition(entityPredicate(EntitySpider.class))
            .chance(0.15F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntityCaveSpider.class))
            .chance(0.2F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntitySkeleton.class))
            .chance(0.03F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntityGuardian.class))
            .chance(0.05F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntityElderGuardian.class))
            .chance(0.08F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntitySlime.class))
            .chance(0.03F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntityStray.class))
            .chance(0.04F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntitySilverfish.class))
            .chance(0.06F)
            .pop()
            .effectBuilder()
            .when(41, 70)
            .executes(addEffect(() -> new PotionEffect(MobEffects.POISON, 60, 0, false, false)))
            .pop()
            .effectBuilder()
            .when(71, 85)
            .executes(addEffect(() -> new PotionEffect(MobEffects.POISON, 60, 1, false, false)))
            .pop()
            .effectBuilder()
            .when(86, 99)
            .executes(player -> {
                if (!player.world.isRemote) {
                    player.addPotionEffect(new PotionEffect(MobEffects.POISON, 60, 1, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 120, 0, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 120, 0, false, false));
                }
            })
            .pop()
            .effectBuilder()
            .when(100)
            .executes(player -> player.attackEntityFrom(PoisonDebuff.POISON_DAMAGE, 15))
            .pop()
            .build();

    public static final DebuffType<InfectionDebuff> INFECTION = DebuffType.TypeBuilder.create(InfectionDebuff::new)
            .name("infection")
            .cap(200)
            .addHitData()
            .condition(entityPredicate(EntityZombieVillager.class))
            .chance(0.06F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntityEnderman.class))
            .chance(0.02F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntityVindicator.class))
            .chance(0.03F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntityWitherSkeleton.class))
            .chance(0.07F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntityHusk.class))
            .chance(0.07F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntityZombie.class))
            .chance(0.06F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntityPigZombie.class))
            .chance(0.09F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntitySkeleton.class))
            .chance(0.03F)
            .pop()
            .addHitData()
            .condition(entityPredicate(EntityStray.class))
            .chance(0.03F)
            .pop()
            .effectBuilder()
            .when(36, 60)
            .executes(player -> {
                if(!player.world.isRemote) {
                    player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 60, 0, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 60, 4, false, false));
                }
            })
            .pop()
            .effectBuilder()
            .when(61, 85)
            .executes(player -> {
                if(!player.world.isRemote) {
                    player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 60, 1, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 60, 10, false, false));
                }
            })
            .pop()
            .effectBuilder()
            .when(86, 99)
            .executes(player -> {
                if(!player.world.isRemote) {
                    player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 60, 2, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 60, 15, false, false));
                }
            })
            .pop()
            .effectBuilder()
            .when(100)
            .executes(player -> player.attackEntityFrom(InfectionDebuff.INFECTION_DAMAGE, 15))
            .pop()
            .build();

    public static final DebuffType<BrokenBoneDebuff> BROKEN_BONE = DebuffType.TypeBuilder.create(BrokenBoneDebuff::new)
            .name("broken_bone")
            .cap(240)
            .addHitData()
            .condition(src -> src instanceof EntityDamageSource && !src.isExplosion())
            .chance(0.01F)
            .pop()
            .addHitData()
            .condition(src -> src.getDamageType().equals("fall"))
            .chance(0.2F)
            .pop()
            .addHitData()
            .condition(DamageSource::isExplosion)
            .chance(0.33F)
            .pop()
            .effectBuilder()
            .when(0, 30)
            .executes(addEffect(() -> new PotionEffect(MobEffects.SLOWNESS, 60, 0, false, false)))
            .pop()
            .effectBuilder()
            .when(31, 55)
            .executes(player -> {
                if(!player.world.isRemote) {
                    player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 1, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 60, 0, false, false));
                }
            })
            .pop()
            .effectBuilder()
            .when(56, 75)
            .executes(player -> {
                if(!player.world.isRemote) {
                    player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 2, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 60, 0, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 60, 0, false, false));
                }
            })
            .pop()
            .effectBuilder()
            .when(76, 99)
            .executes(player -> {
                if(!player.world.isRemote) {
                    player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 3, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 60, 0, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 60, 1, false, false));
                }
            })
            .pop()
            .effectBuilder()
            .when(100)
            .executes(player -> {
                if(!player.world.isRemote) {
                    player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 3, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 60, 1, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 60, 0, false, false));
                    player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 60, 2, false, false));
                }
            })
            .pop()
            .build();

    public static Consumer<EntityPlayer> addEffect(Supplier<PotionEffect> effectSupplier) {
        return player -> {
            if (!player.world.isRemote) {
                player.addPotionEffect(effectSupplier.get());
            }
        };
    }

    public static Predicate<DamageSource> entityPredicate(Class<? extends Entity> cl) {
        return src -> src instanceof EntityDamageSource && src.getTrueSource() != null && src.getTrueSource().getClass().equals(cl);
    }

    public static Predicate<DamageSource> alwaysTrue() {
        return src -> true;
    }

    public static Predicate<DamageSource> alwaysFalse() {
        return src -> false;
    }
}
