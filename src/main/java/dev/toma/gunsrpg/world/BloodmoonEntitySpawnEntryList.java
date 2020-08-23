package dev.toma.gunsrpg.world;

import dev.toma.gunsrpg.common.entity.EntityBloodmoonGolem;
import dev.toma.gunsrpg.common.entity.EntityRocketAngel;
import dev.toma.gunsrpg.config.GRPGConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class BloodmoonEntitySpawnEntryList {

    private static final List<IBloodmoonSpawnEntry> entryList = new ArrayList<>();
    private static final Random random = new Random();

    public static void register(Class<? extends Entity> eclass, int chance, BiFunction<World, Entity, EntityLivingBase> function) {
        entryList.add(new IBloodmoonSpawnEntry.Factory(eclass, chance, function));
    }

    public static EntityLivingBase createEntity(Entity entity, World world) {
        for(IBloodmoonSpawnEntry entry : entryList) {
            if(entry.condition().test(entity.getClass())) {
                if(random.nextInt(20) < entry.chance()) {
                    return entry.spawn(world, entity);
                }
            }
        }
        return null;
    }

    static {
        register(EntitySpider.class, 7, (world, entity) -> {
            EntityCaveSpider spider = new EntityCaveSpider(world);
            spider.setPosition(entity.posX, entity.posY, entity.posZ);
            return spider;
        });
        register(EntitySpider.class, GRPGConfig.worldConfig.rocketAngelSpawnChance, (world, entity) -> {
            EntityRocketAngel rocketAngel = new EntityRocketAngel(world);
            rocketAngel.setPosition(entity.posX, entity.posY, entity.posZ);
            return rocketAngel;
        });
        register(EntityZombie.class, 4, (world, entity) -> {
            EntityBlaze blaze = new EntityBlaze(world);
            blaze.setPosition(entity.posX, entity.posY, entity.posZ);
            return blaze;
        });
        register(EntitySkeleton.class, 10, (world, entity) -> {
            EntityWitherSkeleton witherSkeleton = new EntityWitherSkeleton(world);
            witherSkeleton.setPosition(entity.posX, entity.posY, entity.posZ);
            witherSkeleton.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
            return witherSkeleton;
        });
        register(EntityZombie.class, 4, (world, entity) -> {
            EntityBloodmoonGolem golem = new EntityBloodmoonGolem(world);
            golem.setPosition(entity.posX, entity.posY, entity.posZ);
            return golem;
        });
    }
}
