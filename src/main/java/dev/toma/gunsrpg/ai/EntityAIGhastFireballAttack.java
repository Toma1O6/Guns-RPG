package dev.toma.gunsrpg.ai;

import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityAIGhastFireballAttack extends Goal {

    private final GhastEntity ghast;
    public int attackTimer;

    public EntityAIGhastFireballAttack(GhastEntity ghast) {
        this.ghast = ghast;
    }

    @Override
    public boolean canUse() {
        return ghast.getTarget() != null;
    }

    @Override
    public void start() {
        this.attackTimer = 0;
    }

    @Override
    public void stop() {
        this.ghast.setCharging(false);
    }

    @Override
    public void tick() {
        LivingEntity target = ghast.getTarget();
        double maxRange = 4096.0D;
        if(target.distanceToSqr(ghast) < maxRange && (ghast.canSee(target) || WorldDataFactory.isBloodMoon(ghast.level))) {
            World world = ghast.level;
            ++attackTimer;
            if(attackTimer == 10) {
                world.levelEvent(null, 1015, ghast.blockPosition(), 0);
            }
            if(attackTimer == 20) {
                double d = 4.0D;
                Vector3d look = ghast.getViewVector(1.0F);
                double x = target.getX() - (ghast.getX() + look.x * d);
                double y = target.getY(0.5D) - (0.5 + ghast.getY(0.5D));
                double z = target.getZ() - (ghast.getZ() + look.z * d);
                if (!ghast.isSilent()) {
                    world.levelEvent(null, 1016, ghast.blockPosition(), 0);
                }

                FireballEntity fireball = new FireballEntity(world, ghast, x, y, z);
                fireball.explosionPower = ghast.getExplosionPower();
                fireball.setPos(this.ghast.getX() + look.x * 4.0D, this.ghast.getY(0.5D) + 0.5D, fireball.getZ() + look.z * 4.0D);
                world.addFreshEntity(fireball);
                attackTimer = -40;
            }
        } else if(attackTimer > 0) {
            attackTimer--;
        }
        ghast.setCharging(attackTimer > 10);
    }
}
