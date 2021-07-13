package dev.toma.gunsrpg.world;

import dev.toma.gunsrpg.common.entity.BloodmoonGolemEntity;
import dev.toma.gunsrpg.common.entity.RocketAngelEntity;
import dev.toma.gunsrpg.common.init.GRPGEntityTypes;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.*;
import java.util.function.BiFunction;

public class MobSpawnManager {

    private static final MobSpawnManager INSTANCE = new MobSpawnManager();
    private final List<EntityType<?>> healthExlusions = new ArrayList<>();
    private final Map<EntityType<?>, BooleanConsumer<? extends Entity>> postSpawn = new HashMap<>();
    private final Map<EntityType<?>, List<Pair<Integer, BiFunction<ServerWorld, Vector3d, LivingEntity>>>> bloodmoonEntries = new HashMap<>();
    private final AttributeModifier health2x = new AttributeModifier(UUID.fromString("80096B27-0A64-47FF-A22A-06146FC42448"), "health2x", 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private final AttributeModifier health3x = new AttributeModifier(UUID.fromString("AF5943C6-D3BC-4AD9-8CBB-E16D17D0C245"), "health3x", 2.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);

    public static MobSpawnManager instance() {
        return INSTANCE;
    }

    public void initialize() {
        healthExlusions.add(GRPGEntityTypes.ROCKET_ANGEL.get());
        healthExlusions.add(GRPGEntityTypes.BLOODMOON_GOLEM.get());
        healthExlusions.add(EntityType.ENDER_DRAGON);
        healthExlusions.add(GRPGEntityTypes.GOLD_DRAGON.get());
        healthExlusions.add(EntityType.WITHER);
        registerBloodmoonEntry(EntityType.SPIDER, 7, (world, vec3d) -> {
            CaveSpiderEntity spider = new CaveSpiderEntity(EntityType.CAVE_SPIDER, world);
            spider.setPos(vec3d.x, vec3d.y, vec3d.z);
            return spider;
        });
        registerBloodmoonEntry(EntityType.SPIDER, GRPGConfig.worldConfig.rocketAngelSpawnChance.get(), (world, vec3d) -> {
            RocketAngelEntity rocketAngel = new RocketAngelEntity(world);
            rocketAngel.setPos(vec3d.x, vec3d.y, vec3d.z);
            return rocketAngel;
        });
        registerBloodmoonEntry(EntityType.ZOMBIE, 4, (world, vec3d) -> {
            BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, world);
            blaze.setPos(vec3d.x, vec3d.y, vec3d.z);
            return blaze;
        });
        registerBloodmoonEntry(EntityType.ZOMBIE, 4, (world, vec3d) -> {
            BloodmoonGolemEntity golem = new BloodmoonGolemEntity(world);
            golem.setPos(vec3d.x, vec3d.y, vec3d.z);
            return golem;
        });
        registerBloodmoonEntry(EntityType.SKELETON, 10, (world, vec3d) -> {
            WitherSkeletonEntity witherSkeleton = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, world);
            witherSkeleton.setPos(vec3d.x, vec3d.y, vec3d.z);
            witherSkeleton.finalizeSpawn(world, world.getCurrentDifficultyAt(new BlockPos(vec3d.x, vec3d.y, vec3d.z)), SpawnReason.NATURAL, null, null);
            return witherSkeleton;
        });
        registerPostSpawnAction(EntityType.CREEPER, (bloodmoon, creeper) -> {
            if(creeper.level.isClientSide || !bloodmoon) return;
            if(creeper.getRandom().nextFloat() <= 0.2F) {
                creeper.thunderHit((ServerWorld) creeper.level, null);
                creeper.clearFire();
                creeper.setHealth(creeper.getMaxHealth());
            }
            creeper.maxSwell = 10;
        });
    }

    @SuppressWarnings("unchecked")
    public void processSpawn(LivingEntity entity, World world, boolean isBloodmoon, EntityJoinWorldEvent event) {
        AttributeModifierManager manager = entity.getAttributes();
        if(isBloodmoon && !world.isClientSide) {
            List<Pair<Integer, BiFunction<ServerWorld, Vector3d, LivingEntity>>> list = ModUtils.getNonnullFromMap(bloodmoonEntries, entity.getType(), Collections.emptyList());
            Random random = world.getRandom();
            Vector3d vec3d = entity.position();
            for(Pair<Integer, BiFunction<ServerWorld, Vector3d, LivingEntity>> pair : list) {
                if(random.nextInt(20) < pair.getLeft()) {
                    event.setCanceled(true);
                    entity = pair.getRight().apply((ServerWorld) world, vec3d);
                    world.addFreshEntity(entity);
                    break;
                }
            }
        }
        if(isExluded(entity)) {
            return;
        }
        ModifiableAttributeInstance instance = manager.getInstance(Attributes.MAX_HEALTH);
        instance.removeModifier(health2x);
        instance.removeModifier(health3x);
        AttributeModifier modifier = getRandomModifier(world.getRandom());
        if(modifier != null) {
            instance.addTransientModifier(modifier);
            entity.setHealth(entity.getMaxHealth());
        }
        BooleanConsumer<Entity> consumer = (BooleanConsumer<Entity>) postSpawn.get(entity.getType());
        if(consumer != null) {
            consumer.acceptBoolean(isBloodmoon, entity);
        }
    }

    private boolean isExluded(Entity entity) {
        return healthExlusions.contains(entity.getType());
    }

    private <T extends Entity> void registerPostSpawnAction(EntityType<T> type, BooleanConsumer<T> action) {
        postSpawn.put(type, action);
    }

    private void registerBloodmoonEntry(EntityType<?> type, int chance, BiFunction<ServerWorld, Vector3d, LivingEntity> replacement) {
        List<Pair<Integer, BiFunction<ServerWorld, Vector3d, LivingEntity>>> list = bloodmoonEntries.computeIfAbsent(type, cl -> new ArrayList<>());
        list.add(Pair.of(Math.max(1, chance), replacement));
    }

    private AttributeModifier getRandomModifier(Random random) {
        float f = random.nextFloat();
        return f <= 0.2F ? health3x : f <= 0.5F ? health2x : null;
    }

    @FunctionalInterface
    public interface BooleanConsumer<T> {
        void acceptBoolean(boolean bool, T t);
    }
}
