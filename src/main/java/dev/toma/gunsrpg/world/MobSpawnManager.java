package dev.toma.gunsrpg.world;

import dev.toma.gunsrpg.ai.EntityAIFindClosestPlayer;
import dev.toma.gunsrpg.ai.EntityAIGhastFireballAttack;
import dev.toma.gunsrpg.common.CommonEventHandler;
import dev.toma.gunsrpg.common.entity.EntityBloodmoonGolem;
import dev.toma.gunsrpg.common.entity.EntityGoldDragon;
import dev.toma.gunsrpg.common.entity.EntityRocketAngel;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.*;
import java.util.function.BiFunction;

public class MobSpawnManager {

    private static final MobSpawnManager INSTANCE = new MobSpawnManager();
    private final List<Class<? extends Entity>> healthExlusions = new ArrayList<>();
    private final Map<Class<? extends Entity>, BooleanConsumer<? extends Entity>> postSpawn = new HashMap<>();
    private final Map<Class<? extends Entity>, List<Pair<Integer, BiFunction<World, Vec3d, EntityLiving>>>> bloodmoonEntries = new HashMap<>();
    private final AttributeModifier health2x = new AttributeModifier(UUID.fromString("80096B27-0A64-47FF-A22A-06146FC42448"), "health2x", 1.0D, 2);
    private final AttributeModifier health3x = new AttributeModifier(UUID.fromString("AF5943C6-D3BC-4AD9-8CBB-E16D17D0C245"), "health3x", 2.0D, 2);
    private final AttributeModifier bloodmoonFollowRange = new AttributeModifier(UUID.fromString("D2CA5ACB-5C00-49E3-AB1A-F1605CE0D7B5"), "bloodmoonFollowRange", GRPGConfig.worldConfig.bloodMoonMobAgroRange, 0);

    public static MobSpawnManager instance() {
        return INSTANCE;
    }

    public void initialize() {
        healthExlusions.add(EntityRocketAngel.class);
        healthExlusions.add(EntityBloodmoonGolem.class);
        healthExlusions.add(EntityDragon.class);
        healthExlusions.add(EntityGoldDragon.class);
        healthExlusions.add(EntityWither.class);
        registerBloodmoonEntry(EntitySpider.class, 7, (world, vec3d) -> {
            EntityCaveSpider spider = new EntityCaveSpider(world);
            spider.setPosition(vec3d.x, vec3d.y, vec3d.z);
            return spider;
        });
        registerBloodmoonEntry(EntitySpider.class, GRPGConfig.worldConfig.rocketAngelSpawnChance, (world, vec3d) -> {
            EntityRocketAngel rocketAngel = new EntityRocketAngel(world);
            rocketAngel.setPosition(vec3d.x, vec3d.y, vec3d.z);
            return rocketAngel;
        });
        registerBloodmoonEntry(EntityZombie.class, 4, (world, vec3d) -> {
            EntityBlaze blaze = new EntityBlaze(world);
            blaze.setPosition(vec3d.x, vec3d.y, vec3d.z);
            return blaze;
        });
        registerBloodmoonEntry(EntityZombie.class, 4, (world, vec3d) -> {
            EntityBloodmoonGolem golem = new EntityBloodmoonGolem(world);
            golem.setPosition(vec3d.x, vec3d.y, vec3d.z);
            return golem;
        });
        registerBloodmoonEntry(EntitySkeleton.class, 10, (world, vec3d) -> {
            EntityWitherSkeleton witherSkeleton = new EntityWitherSkeleton(world);
            witherSkeleton.setPosition(vec3d.x, vec3d.y, vec3d.z);
            witherSkeleton.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(vec3d.x, vec3d.y, vec3d.z)), null);
            return witherSkeleton;
        });
        registerPostSpawnAction(EntityZombie.class, (bloodmoon, zombie) -> {
            if(zombie.world.isRemote || !bloodmoon) return;
            zombie.targetTasks.taskEntries.removeIf(entry -> entry.priority == 2 && entry.action instanceof EntityAINearestAttackableTarget);
            zombie.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(zombie, EntityPlayer.class, false));
            zombie.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100000, 1, false, false));
        });
        registerPostSpawnAction(EntitySkeleton.class, (bloodmoon, skeleton) -> {
            if(skeleton.world.isRemote || !bloodmoon) return;
            skeleton.targetTasks.taskEntries.removeIf(entry -> entry.priority == 2 && entry.action instanceof EntityAINearestAttackableTarget);
            skeleton.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(skeleton, EntityPlayer.class, false));
        });
        registerPostSpawnAction(EntityCreeper.class, (bloodmoon, creeper) -> {
            if(creeper.world.isRemote || !bloodmoon) return;
            creeper.targetTasks.taskEntries.removeIf(entry -> entry.priority == 1 && entry.action instanceof EntityAINearestAttackableTarget);
            creeper.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(creeper, EntityPlayer.class, false));
            if(CommonEventHandler.random.nextFloat() <= 0.2F) {
                creeper.onStruckByLightning(null);
                creeper.extinguish();
                creeper.setHealth(creeper.getMaxHealth());
            }
            creeper.fuseTime = 10;
        });
        registerPostSpawnAction(EntityGhast.class, (bloodmoon, ghast) -> {
            if(ghast.world.isRemote) return;
            ghast.tasks.taskEntries.removeIf(entry -> entry.priority == 7 && entry.action instanceof EntityGhast.AIFireballAttack);
            ghast.tasks.addTask(7, new EntityAIGhastFireballAttack(ghast));
            ghast.targetTasks.taskEntries.removeIf(entry -> entry.priority == 1 && entry.action instanceof EntityAIFindEntityNearestPlayer);
            ghast.targetTasks.addTask(1, new EntityAIFindClosestPlayer(ghast));
        });
    }

    @SuppressWarnings("unchecked")
    public void processSpawn(EntityLivingBase entity, World world, boolean isBloodmoon, EntityJoinWorldEvent event) {
        AbstractAttributeMap map = entity.getAttributeMap();
        if(isBloodmoon) {
            IAttributeInstance instance = map.getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE);
            instance.removeModifier(bloodmoonFollowRange);
            instance.applyModifier(bloodmoonFollowRange);
            List<Pair<Integer, BiFunction<World, Vec3d, EntityLiving>>> list = ModUtils.getNonnullFromMap(bloodmoonEntries, entity.getClass(), Collections.emptyList());
            Random random = world.rand;
            Vec3d vec3d = entity.getPositionVector();
            for(Pair<Integer, BiFunction<World, Vec3d, EntityLiving>> pair : list) {
                if(random.nextInt(20) < pair.getLeft()) {
                    event.setCanceled(true);
                    entity = pair.getRight().apply(world, vec3d);
                    if(!world.isRemote) {
                        world.spawnEntity(entity);
                    }
                    break;
                }
            }
        }
        if(isExluded(entity)) {
            return;
        }
        IAttributeInstance instance = map.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
        instance.removeModifier(health2x);
        instance.removeModifier(health3x);
        AttributeModifier modifier = getRandomModifier(world.rand);
        if(modifier != null) {
            instance.applyModifier(modifier);
            entity.setHealth(entity.getMaxHealth());
        }
        BooleanConsumer<Entity> consumer = (BooleanConsumer<Entity>) postSpawn.get(entity.getClass());
        if(consumer != null) {
            consumer.acceptBoolean(isBloodmoon, entity);
        }
    }

    private boolean isExluded(Entity entity) {
        return healthExlusions.contains(entity.getClass());
    }

    private <T extends Entity> void registerPostSpawnAction(Class<T> tClass, BooleanConsumer<T> action) {
        postSpawn.put(tClass, action);
    }

    private void registerBloodmoonEntry(Class<? extends Entity> eClass, int chance, BiFunction<World, Vec3d, EntityLiving> replacement) {
        List<Pair<Integer, BiFunction<World, Vec3d, EntityLiving>>> list = bloodmoonEntries.computeIfAbsent(eClass, cl -> new ArrayList<>());
        list.add(Pair.of(Math.max(1, chance), replacement));
        bloodmoonEntries.put(eClass, list);
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
