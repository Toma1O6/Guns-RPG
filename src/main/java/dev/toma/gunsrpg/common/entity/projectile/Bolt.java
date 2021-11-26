package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class Bolt extends Bullet {

    public Bolt(EntityType<? extends Bolt> type, World world) {
        super(type, world);
    }

    public Bolt(EntityType<? extends Bolt> type, World world, LivingEntity owner) {
        super(type, world, owner);
    }
}
