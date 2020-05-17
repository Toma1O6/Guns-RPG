package dev.toma.gunsrpg.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface IBloodmoonSpawnEntry {

    Predicate<Class<? extends Entity>> condition();

    int chance();

    EntityLivingBase spawn(World world, Entity old);

    class Factory implements IBloodmoonSpawnEntry {

        final Predicate<Class<? extends Entity>> predicate;
        final int chance;
        final BiFunction<World, Entity, EntityLivingBase> function;

        public Factory(Class<? extends Entity> eClass, int chance, BiFunction<World, Entity, EntityLivingBase> spawner) {
            this.predicate = aClass -> aClass.equals(eClass);
            this.chance = chance;
            this.function = spawner;
        }

        @Override
        public Predicate<Class<? extends Entity>> condition() {
            return predicate;
        }

        @Override
        public int chance() {
            return chance;
        }

        @Override
        public EntityLivingBase spawn(World world, Entity old) {
            return function.apply(world, old);
        }
    }
}
