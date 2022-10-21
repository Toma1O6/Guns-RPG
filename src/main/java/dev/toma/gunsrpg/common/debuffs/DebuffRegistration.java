package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.init.Debuffs;
import dev.toma.gunsrpg.common.init.ModDamageSources;
import dev.toma.gunsrpg.common.init.WeaponDamageSource;
import dev.toma.gunsrpg.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class DebuffRegistration {

    private DebuffRegistration() {
    }

    public static DebuffType<?> createPoisonType() {
        return new StagedDebuffType.StagedBuilder<>()
                .factory(type -> new StagedDebuff((StagedDebuffType<?>) type))
                .disableOn(() -> ModConfig.debuffConfig.disablePoison())
                .delay(Attribs.POISON_DELAY)
                .resistance(Attribs.POISON_RESISTANCE)
                .blockingAttribute(Attribs.POISON_BLOCK)
                .stage(40, DebuffRegistration::doNothing)
                .stage(70, DebuffRegistration::applyPoisonStage2)
                .stage(85, DebuffRegistration::applyPoisonStage3)
                .stage(99, DebuffRegistration::applyPoisonStage4)
                .stage(100, DebuffRegistration::applyPoisonStage5)
                .addApplyConstraint(context -> forEntity(EntityType.SPIDER, context, 0.15F))
                .addApplyConstraint(context -> forEntity(EntityType.CAVE_SPIDER, context, 0.2F))
                .addApplyConstraint(context -> forEntity(EntityType.SKELETON, context, 0.02F))
                .addApplyConstraint(context -> forEntity(EntityType.GUARDIAN, context, 0.05F))
                .addApplyConstraint(context -> forEntity(EntityType.ELDER_GUARDIAN, context, 0.08F))
                .addApplyConstraint(context -> forEntityDynamic(EntityType.SLIME, context, SlimeEntity::getSize, size -> 0.03F * size))
                .build()
                .setRegistryName("poison");
    }

    public static DebuffType<?> createInfectionType() {
        return new StagedDebuffType.StagedBuilder<>()
                .factory(type -> new StagedDebuff((StagedDebuffType<?>) type))
                .disableOn(() -> ModConfig.debuffConfig.disableInfection())
                .delay(Attribs.INFECTION_DELAY)
                .resistance(Attribs.INFECTION_RESISTANCE)
                .blockingAttribute(Attribs.INFECTION_BLOCK)
                .stage(35, DebuffRegistration::doNothing)
                .stage(60, DebuffRegistration::applyInfectionStage2)
                .stage(85, DebuffRegistration::applyInfectionStage3)
                .stage(99, DebuffRegistration::applyInfectionStage4)
                .stage(100, DebuffRegistration::applyInfectionStage5)
                .addApplyConstraint(context -> forEntity(EntityType.ZOMBIE_VILLAGER, context, 0.06F))
                .addApplyConstraint(context -> forEntity(EntityType.ENDERMAN, context, 0.02F))
                .addApplyConstraint(context -> forEntity(EntityType.VINDICATOR, context, 0.03F))
                .addApplyConstraint(context -> forEntity(EntityType.WITHER_SKELETON, context, 0.07F))
                .addApplyConstraint(context -> forEntity(EntityType.HUSK, context, 0.07F))
                .addApplyConstraint(context -> forEntity(EntityType.ZOMBIE, context, 0.06F))
                .addApplyConstraint(context -> forEntity(EntityType.ZOMBIFIED_PIGLIN, context, 0.09F))
                .build()
                .setRegistryName("infection");
    }

    public static DebuffType<?> createFractureType() {
        return new StagedDebuffType.StagedBuilder<>()
                .factory(type -> new StagedDebuff((StagedDebuffType<?>) type))
                .disableOn(() -> ModConfig.debuffConfig.disableFractures())
                .delay(Attribs.FRACTURE_DELAY)
                .resistance(Attribs.FRACTURE_RESISTANCE)
                .blockingAttribute(Attribs.FRACTURE_BLOCK)
                .stage(30, DebuffRegistration::applyFractureStage1)
                .stage(55, DebuffRegistration::applyFractureStage2)
                .stage(75, DebuffRegistration::applyFractureStage3)
                .stage(99, DebuffRegistration::applyFractureStage4)
                .stage(100, DebuffRegistration::applyFractureStage5)
                .addApplyConstraint(DebuffRegistration::genericDamageConstraint)
                .addApplyConstraint(DebuffRegistration::explosionConstraint)
                .addApplyConstraint(DebuffRegistration::fallConstraint)
                .build()
                .setRegistryName("fracture");
    }

    public static DebuffType<?> createBleedType() {
        return new StagedDebuffType.StagedBuilder<>()
                .factory(type -> new StagedDebuff((StagedDebuffType<?>) type))
                .disableOn(() -> ModConfig.debuffConfig.disableBleeding())
                .delay(Attribs.BLEED_DELAY)
                .resistance(Attribs.BLEED_RESISTANCE)
                .blockingAttribute(Attribs.BLEED_BLOCK)
                .stage(25, DebuffRegistration::applyBleedingStage1)
                .stage(50, DebuffRegistration::applyBleedingStage2)
                .stage(75, DebuffRegistration::applyBleedingStage3)
                .stage(99, DebuffRegistration::applyBleedingStage4)
                .stage(100, DebuffRegistration::applyBleedingStage5)
                .addApplyConstraint(context -> forEntity(EntityType.SPIDER, context, 0.05F))
                .addApplyConstraint(context -> forEntity(EntityType.ZOMBIE, context, 0.06F))
                .addApplyConstraint(context -> forEntity(EntityType.ZOMBIE_VILLAGER, context, 0.06F))
                .addApplyConstraint(context -> forEntity(EntityType.STRAY, context, 0.07F))
                .addApplyConstraint(context -> forEntity(EntityType.SKELETON, context, 0.07F))
                .addApplyConstraint(context -> forEntity(EntityType.ENDERMAN, context, 0.05F))
                .addApplyConstraint(context -> forEntity(EntityType.ZOMBIFIED_PIGLIN, context, 0.09F))
                .addApplyConstraint(context -> forEntity(EntityType.WITHER_SKELETON, context, 0.08F))
                .addApplyConstraint(context -> forSource(context, DamageSource::isExplosion, 0.13F))
                .addApplyConstraint(context -> forSource(context, src -> src == DamageSource.FALL, 0.05F))
                .addApplyConstraint(context -> forSource(context, src -> src instanceof WeaponDamageSource, 0.05F))
                .build()
                .setRegistryName("bleed");
    }

    public static DebuffType<?> createRespawnType() {
        return new RespawnDebuffType.RespawnDebuffBuilder<>()
                .factory(type -> new ReducedHealthDebuff((RespawnDebuffType<?>) type))
                .disableOn(() -> ModConfig.debuffConfig.disableRespawnDebuff())
                .duration(3600)
                .build()
                .setRegistryName("respawn");
    }

    public static DebuffType<?> createPoisonBlockType() {
        return new DummyDebuffType.DummyBuilder<>()
                .linkedTo(() -> Debuffs.POISON)
                .factory(DummyDebuff::new)
                .build()
                .setRegistryName("poison_block");
    }

    public static DebuffType<?> createInfectionBlockType() {
        return new DummyDebuffType.DummyBuilder<>()
                .linkedTo(() -> Debuffs.INFECTION)
                .factory(DummyDebuff::new)
                .build()
                .setRegistryName("infection_block");
    }

    public static DebuffType<?> createFractureBlockType() {
        return new DummyDebuffType.DummyBuilder<>()
                .linkedTo(() -> Debuffs.FRACTURE)
                .factory(DummyDebuff::new)
                .build()
                .setRegistryName("fracture_block");
    }

    public static DebuffType<?> createBleedBlockType() {
        return new DummyDebuffType.DummyBuilder<>()
                .linkedTo(() -> Debuffs.BLEED)
                .factory(DummyDebuff::new)
                .build()
                .setRegistryName("bleed_block");
    }

    // CONSTRAINTS

    private static void doNothing(PlayerEntity player) {
        // actually does nothing
    }

    private static float genericDamageConstraint(IDebuffContext context) {
        DamageSource source = context.getSource();
        return source instanceof EntityDamageSource && !source.isExplosion() && !source.isProjectile() ? 0.01F : 0.0F;
    }

    private static float explosionConstraint(IDebuffContext context) {
        float amount = context.getReceivedDamage();
        float multiplier = amount / 5.0F;
        return context.getSource().isExplosion() ? multiplier * 0.2F * (1.0F - context.getData().getAttributes().getAttribute(Attribs.EXPLOSION_RESISTANCE).floatValue()) : 0.0F;
    }

    private static float fallConstraint(IDebuffContext context) {
        DamageSource source = context.getSource();
        float amount = context.getReceivedDamage();
        float multiplier = amount / 4.0F;
        if (source == DamageSource.FALL) {
            return Math.max(0.0F, multiplier * 0.3F * context.getData().getAttributes().getAttribute(Attribs.FALL_RESISTANCE).floatValue());
        }
        return 0.0F;
    }

    // STAGES

    private static float forSource(IDebuffContext context, Predicate<DamageSource> constraint, float value) {
        return constraint.test(context.getSource()) ? value : 0.0F;
    }

    private static void applyPoisonStage2(PlayerEntity player) {
        applySingle(player, () -> new EffectInstance(Effects.POISON, 60, 0, false, false));
    }

    private static void applyPoisonStage3(PlayerEntity player) {
        applySingle(player, () -> new EffectInstance(Effects.POISON, 60, 1, false, false));
    }

    private static void applyPoisonStage4(PlayerEntity player) {
        applyMultiple(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.POISON, 60, 1, false, false),
                new EffectInstance(Effects.CONFUSION, 260, 0, false, false),
                new EffectInstance(Effects.BLINDNESS, 260, 0, false, false)
        });
    }

    private static void applyPoisonStage5(PlayerEntity player) {
        runServerSideDelayed(player, () -> player.hurt(ModDamageSources.POISON_DAMAGE, 15.0F));
    }

    private static void applyInfectionStage2(PlayerEntity player) {
        applyMultiple(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.WITHER, 60, 0, false, false),
                new EffectInstance(Effects.HUNGER, 60, 4, false, false)
        });
    }

    private static void applyInfectionStage3(PlayerEntity player) {
        applyMultiple(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.WITHER, 60, 1, false, false),
                new EffectInstance(Effects.HUNGER, 60, 9, false, false)
        });
    }

    private static void applyInfectionStage4(PlayerEntity player) {
        applyMultiple(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.WITHER, 60, 2, false, false),
                new EffectInstance(Effects.HUNGER, 60, 14, false, false)
        });
    }

    private static void applyInfectionStage5(PlayerEntity player) {
        runServerSideDelayed(player, () -> player.hurt(ModDamageSources.INFECTION_DAMAGE, 15.0F));
    }

    private static void applyFractureStage1(PlayerEntity player) {
        applyFractureSprintDebuff(player);
        applySingle(player, () -> new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 0, false, false));
    }

    private static void applyFractureStage2(PlayerEntity player) {
        applyFractureSprintDebuff(player);
        applyMultiple(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 1, false, false),
                new EffectInstance(Effects.DIG_SLOWDOWN, 60, 0, false, false)
        });
    }

    private static void applyFractureStage3(PlayerEntity player) {
        applyFractureSprintDebuff(player);
        applyMultiple(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 2, false, false),
                new EffectInstance(Effects.WEAKNESS, 60, 0, false, false),
                new EffectInstance(Effects.DIG_SLOWDOWN, 60, 0, false, false)
        });
    }

    private static void applyFractureStage4(PlayerEntity player) {
        applyFractureSprintDebuff(player);
        applyMultiple(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 3, false, false),
                new EffectInstance(Effects.WEAKNESS, 60, 0, false, false),
                new EffectInstance(Effects.DIG_SLOWDOWN, 60, 1, false, false)
        });
    }

    private static void applyFractureStage5(PlayerEntity player) {
        applyFractureSprintDebuff(player);
        applyMultiple(player, () -> new EffectInstance[]{
                new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 3, false, false),
                new EffectInstance(Effects.WEAKNESS, 60, 1, false, false),
                new EffectInstance(Effects.WITHER, 60, 0, false, false),
                new EffectInstance(Effects.DIG_SLOWDOWN, 60, 2, false, false)
        });
    }

    private static void applyFractureSprintDebuff(PlayerEntity player) {
        runServerSideDelayed(player, 20L, () -> {
            if (player.isSprinting()) {
                player.hurt(ModDamageSources.FRACTURE_DAMAGE, 1);
            }
        });
    }

    private static void applyBleedingStage1(PlayerEntity player) {
        applyBleedDamage(player, 120L);
    }

    private static void applyBleedingStage2(PlayerEntity player) {
        applyBleedDamage(player, 80L);
    }

    private static void applyBleedingStage3(PlayerEntity player) {
        applyBleedDamage(player, 60L);
    }

    private static void applyBleedingStage4(PlayerEntity player) {
        applyBleedDamage(player, 30L);
    }

    private static void applyBleedingStage5(PlayerEntity player) {
        applyBleedDamage(player, 15L);
    }

    // UTILITY METHODS

    private static void applySingle(PlayerEntity player, Supplier<EffectInstance> instanceSupplier) {
        runServerSideDelayed(player, () -> player.addEffect(instanceSupplier.get()));
    }

    private static void applyMultiple(PlayerEntity player, Supplier<EffectInstance[]> instanceSupplier) {
        runServerSideDelayed(player, () -> {
            for (EffectInstance effectInstance : instanceSupplier.get()) {
                player.addEffect(effectInstance);
            }
        });
    }

    private static void applyBleedDamage(PlayerEntity player, long delay) {
        runServerSideDelayed(player, delay, () -> {
            player.hurt(ModDamageSources.BLEED_DAMAGE, 1.0F);
            if (delay < 20L) {
                player.invulnerableTime = 0;
            }
        });
    }

    private static void runServerSideDelayed(PlayerEntity player, Runnable action) {
        runServerSideDelayed(player, 50L, action);
    }

    private static void runServerSideDelayed(PlayerEntity player, long delay, Runnable action) {
        World level = player.level;
        if (!level.isClientSide() && level.getGameTime() % delay == 0L) {
            action.run();
        }
    }

    private static boolean isEntity(EntityType<?> type, IDebuffContext context) {
        DamageSource source = context.getSource();
        return source instanceof EntityDamageSource && !((EntityDamageSource) source).isThorns() && source.getDirectEntity() != null && source.getDirectEntity().getType() == type;
    }

    private static float forEntity(EntityType<?> type, IDebuffContext context, float value) {
        return isEntity(type, context) ? value : 0.0F;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Entity, V> float forEntityDynamic(EntityType<E> type, IDebuffContext context, Function<E, V> fetcher, Function<V, Float> calculator) {
        DamageSource source = context.getSource();
        if (source instanceof EntityDamageSource) {
            EntityDamageSource entityDamageSource = (EntityDamageSource) source;
            if (entityDamageSource.isThorns()) {
                return 0.0F;
            }
            Entity direct = source.getDirectEntity();
            if (direct != null && direct.getType() == type) {
                E ent = (E) direct;
                V value = fetcher.apply(ent);
                return calculator.apply(value);
            }
        }
        return 0.0F;
    }
}
