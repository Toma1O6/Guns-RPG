package dev.toma.gunsrpg.world;

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
                if(random.nextInt(entry.chance()) == 0) {
                    return entry.spawn(world, entity);
                }
            }
        }
        return null;
    }

    static {
        register(EntitySpider.class, 5, (world, entity) -> {
            EntityCaveSpider spider = new EntityCaveSpider(world);
            spider.setPosition(entity.posX, entity.posY, entity.posZ);
            return spider;
        });
        register(EntityZombie.class, 3, (world, entity) -> {
            EntityBlaze blaze = new EntityBlaze(world);
            blaze.setPosition(entity.posX, entity.posY, entity.posZ);
            return blaze;
        });
        register(EntityCreeper.class, 30, (world, entity) -> {
            EntityGhast ghast = new EntityGhast(world);
            ghast.setPosition(entity.posX, Math.max(world.getSeaLevel(), entity.posY + 5), entity.posZ);
            return ghast;
        });
        register(EntitySkeleton.class, 10, (world, entity) -> {
            EntityWitherSkeleton witherSkeleton = new EntityWitherSkeleton(world);
            witherSkeleton.setPosition(entity.posX, entity.posY, entity.posZ);
            return witherSkeleton;
        });
    }
}