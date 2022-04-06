package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class Pellet extends Bullet {

    public Pellet(EntityType<? extends Pellet> type, World world) {
        super(type, world);
    }

    public Pellet(EntityType<? extends Pellet> type, World world, LivingEntity owner) {
        super(type, world, owner);
    }

    @Override
    public void postTick() {
        applyGravity(0.15F);
        if (tickCount > 2) {
            mulDamage(0.8F);
        }
    }
}
