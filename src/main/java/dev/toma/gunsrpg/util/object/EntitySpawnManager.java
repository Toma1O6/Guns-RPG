package dev.toma.gunsrpg.util.object;

import dev.toma.gunsrpg.ai.EntityAIFindClosestPlayer;
import dev.toma.gunsrpg.ai.EntityAIGhastFireballAttack;
import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EntitySpawnManager {

    private static final Map<Class<? extends Entity>, Consumer<? extends Entity>> tempMap = new HashMap<>();
    private double healthBase;
    private Consumer<Entity> consumer;

    public EntitySpawnManager(double base) {
        this.healthBase = base;
    }

    public void setHealthBase(double healthBase) {
        this.healthBase = healthBase;
    }

    public double getHealthBase() {
        return healthBase;
    }

    public void setAction(Consumer<Entity> action) {
        this.consumer = action;
    }

    public void runAction(Entity entity) {
        this.consumer.accept(entity);
    }

    public boolean hasCustomAction() {
        return consumer != null;
    }

    @SuppressWarnings("unchecked")
    public static Consumer<Entity> getManager(Class<? extends Entity> aClass) {
        return (Consumer<Entity>) tempMap.get(aClass);
    }

    private static <C extends Entity> void registerEntityConsumer(Class<C> entity, Consumer<C> consumer) {
        tempMap.put(entity, consumer);
    }

    static {
        registerEntityConsumer(EntityZombie.class, zombie -> {
            if(zombie.world.isRemote) return;
            zombie.targetTasks.taskEntries.removeIf(entry -> entry.priority == 2 && entry.action instanceof EntityAINearestAttackableTarget);
            zombie.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(zombie, EntityPlayer.class, false));
            if(WorldDataFactory.isBloodMoon(zombie.world)) {
                zombie.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100000, 1, false, false));
            }
        });
        registerEntityConsumer(EntitySkeleton.class, skeleton -> {
            if(skeleton.world.isRemote) return;
            skeleton.targetTasks.taskEntries.removeIf(entry -> entry.priority == 2 && entry.action instanceof EntityAINearestAttackableTarget);
            skeleton.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(skeleton, EntityPlayer.class, false));
        });
        registerEntityConsumer(EntityCreeper.class, creeper -> {
            if(creeper.world.isRemote) return;
            creeper.targetTasks.taskEntries.removeIf(entry -> entry.priority == 1 && entry.action instanceof EntityAINearestAttackableTarget);
            creeper.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(creeper, EntityPlayer.class, false));
            if(WorldDataFactory.isBloodMoon(creeper.world)) creeper.fuseTime = 1;
        });
        registerEntityConsumer(EntityGhast.class, ghast -> {
            if(ghast.world.isRemote) return;
            ghast.tasks.taskEntries.removeIf(entry -> entry.priority == 7 && entry.action instanceof EntityGhast.AIFireballAttack);
            ghast.tasks.addTask(7, new EntityAIGhastFireballAttack(ghast));
            ghast.targetTasks.taskEntries.removeIf(entry -> entry.priority == 1 && entry.action instanceof EntityAIFindEntityNearestPlayer);
            ghast.targetTasks.addTask(1, new EntityAIFindClosestPlayer(ghast));
        });
    }
}
