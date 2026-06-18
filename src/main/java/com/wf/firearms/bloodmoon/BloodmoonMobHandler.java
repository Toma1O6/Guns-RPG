package com.wf.firearms.bloodmoon;

import com.wf.firearms.ai.BloodmoonAggroGoal;
import com.wf.firearms.ai.OpenDoorWithoutClosingGoal;
import com.wf.firearms.config.BloodmoonConfig;
import com.wf.firearms.config.MobSpawnConfig;
import com.wf.firearms.entity.BloodmoonGolemEntity;
import com.wf.firearms.entity.RocketAngelEntity;
import com.wf.firearms.registry.ModEntities;
import com.wf.firearms.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.IntSupplier;

/** 血月刷怪替换、生命倍率与 AI 后处理。 */
public final class BloodmoonMobHandler {
    private static final UUID HEALTH_BOOST_UUID =
            UUID.fromString("80096B27-0A64-47FF-A22A-06146FC42448");
    private static final AttributeModifier HEALTH_2X =
            new AttributeModifier(HEALTH_BOOST_UUID, "gunsrpg_health2x", 1.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final AttributeModifier HEALTH_3X =
            new AttributeModifier(HEALTH_BOOST_UUID, "gunsrpg_health3x", 2.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final AttributeModifier HEALTH_4X =
            new AttributeModifier(HEALTH_BOOST_UUID, "gunsrpg_health4x", 3.0, AttributeModifier.Operation.MULTIPLY_TOTAL);

    private record Replacement(IntSupplier weight, BiFunction<ServerLevel, Vec3, LivingEntity> factory) {}

    private static final List<Replacement> SPIDER_REPLACEMENTS = new ArrayList<>();
    private static final List<Replacement> ZOMBIE_REPLACEMENTS = new ArrayList<>();
    private static final List<Replacement> SKELETON_REPLACEMENTS = new ArrayList<>();

    static {
        SPIDER_REPLACEMENTS.add(
                new Replacement(
                        () -> 7,
                        (world, pos) -> {
                            CaveSpider spider = new CaveSpider(EntityType.CAVE_SPIDER, world);
                            spider.moveTo(pos.x, pos.y, pos.z, world.random.nextFloat() * 360f, 0);
                            return spider;
                        }));
        SPIDER_REPLACEMENTS.add(
                new Replacement(
                        MobSpawnConfig::rocketAngelBloodmoonWeight,
                        (world, pos) -> {
                            RocketAngelEntity angel = ModEntities.ROCKET_ANGEL.get().create(world);
                            angel.moveTo(pos.x, pos.y, pos.z, world.random.nextFloat() * 360f, 0);
                            return angel;
                        }));
        ZOMBIE_REPLACEMENTS.add(
                new Replacement(
                        () -> 4,
                        (world, pos) -> {
                            Blaze blaze = new Blaze(EntityType.BLAZE, world);
                            blaze.moveTo(pos.x, pos.y, pos.z, world.random.nextFloat() * 360f, 0);
                            return blaze;
                        }));
        ZOMBIE_REPLACEMENTS.add(
                new Replacement(
                        () -> 2,
                        (world, pos) -> {
                            BloodmoonGolemEntity golem = ModEntities.BLOODMOON_GOLEM.get().create(world);
                            golem.moveTo(pos.x, pos.y, pos.z, world.random.nextFloat() * 360f, 0);
                            return golem;
                        }));
        SKELETON_REPLACEMENTS.add(
                new Replacement(
                        () -> 10,
                        (world, pos) -> {
                            WitherSkeleton skelly = new WitherSkeleton(EntityType.WITHER_SKELETON, world);
                            skelly.moveTo(pos.x, pos.y, pos.z, world.random.nextFloat() * 360f, 0);
                            skelly.finalizeSpawn(
                                    world,
                                    world.getCurrentDifficultyAt(BlockPos.containing(pos)),
                                    net.minecraft.world.entity.MobSpawnType.NATURAL,
                                    null,
                                    null);
                            return skelly;
                        }));
    }

    private BloodmoonMobHandler() {}

    /** 仅处理自然刷怪，避免进世界加载区块时对成百上千实体重复处理导致卡死。 */
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        Mob mob = event.getEntity();
        LivingEntity living = mob;
        MobSpawnType spawnType = event.getSpawnType();
        if (spawnType != MobSpawnType.NATURAL
                && spawnType != MobSpawnType.PATROL
                && spawnType != MobSpawnType.REINFORCEMENT) {
            return;
        }
        if (!BloodmoonService.isBloodMoon(level)) {
            return;
        }
        if (BloodmoonFlags.isMarked(living)) {
            return;
        }
        if (living instanceof BloodmoonGolemEntity || living instanceof RocketAngelEntity) {
            BloodmoonFlags.mark(living);
            finishSpawn(level, living);
            return;
        }
        List<Replacement> table = replacementsFor(mob.getType());
        RandomSource random = level.getRandom();
        Vec3 pos = living.position();
        for (Replacement entry : table) {
            if (random.nextInt(20) < entry.weight().getAsInt()) {
                LivingEntity replacement = entry.factory().apply(level, pos);
                BloodmoonFlags.mark(replacement);
                level.addFreshEntity(replacement);
                finishSpawn(level, replacement);
                event.setSpawnCancelled(true);
                return;
            }
        }
        BloodmoonFlags.mark(living);
        finishSpawn(level, living);
    }

    private static List<Replacement> replacementsFor(EntityType<?> type) {
        if (type == EntityType.SPIDER) {
            return SPIDER_REPLACEMENTS;
        }
        if (type == EntityType.ZOMBIE) {
            return ZOMBIE_REPLACEMENTS;
        }
        if (type == EntityType.SKELETON) {
            return SKELETON_REPLACEMENTS;
        }
        return List.of();
    }

    static void finishSpawn(ServerLevel level, LivingEntity entity) {
        if (!(entity instanceof BloodmoonGolemEntity) && !(entity instanceof RocketAngelEntity)) {
            applyHealthBuff(entity, level.getRandom());
        }
        if (entity instanceof Creeper creeper) {
            if (level.getRandom().nextFloat() <= 0.2f) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
                if (bolt != null) {
                    bolt.setVisualOnly(true);
                    creeper.thunderHit(level, bolt);
                    creeper.clearFire();
                    creeper.setHealth(creeper.getMaxHealth());
                }
            }
        }
        if (entity instanceof Mob mob) {
            if (BloodmoonService.isBloodMoon(level) && mob.getType().is(ModTags.Entities.BLOODMOON_DOOR_OPENING)) {
                if (mob.getNavigation() instanceof GroundPathNavigation groundNav) {
                    groundNav.setCanOpenDoors(true);
                    mob.goalSelector.addGoal(0, new OpenDoorWithoutClosingGoal(mob));
                }
            }
            mob.targetSelector.addGoal(1, new BloodmoonAggroGoal(mob));
            if (mob instanceof Zombie zombie) {
                zombie.setCanBreakDoors(true);
                AttributeInstance follow = zombie.getAttribute(Attributes.FOLLOW_RANGE);
                if (follow != null) {
                    double range = BloodmoonConfig.bloodMoonMobAgroRange();
                    if (follow.getBaseValue() < range) {
                        follow.setBaseValue(range);
                    }
                }
            }
            mob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(mob, Player.class, false));
        }
    }

    private static void applyHealthBuff(LivingEntity entity, RandomSource random) {
        AttributeInstance health = entity.getAttribute(Attributes.MAX_HEALTH);
        if (health == null) {
            return;
        }
        health.removeModifier(HEALTH_BOOST_UUID);
        AttributeModifier modifier = pickHealthModifier(random);
        if (modifier != null) {
            health.addTransientModifier(modifier);
            entity.setHealth(entity.getMaxHealth());
        }
    }

    private static AttributeModifier pickHealthModifier(RandomSource random) {
        float roll = random.nextFloat();
        if (roll < BloodmoonConfig.health4xChance()) {
            return HEALTH_4X;
        }
        if (roll < BloodmoonConfig.health3xChance()) {
            return HEALTH_3X;
        }
        if (roll < BloodmoonConfig.health2xChance()) {
            return HEALTH_2X;
        }
        return null;
    }
}
