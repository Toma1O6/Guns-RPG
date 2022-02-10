package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class Pebble extends Bullet {

    public Pebble(EntityType<? extends Pebble> type, World world) {
        super(type, world);
    }

    public Pebble(EntityType<? extends Pebble> type, World world, LivingEntity shooter) {
        super(type, world, shooter);
    }
}
