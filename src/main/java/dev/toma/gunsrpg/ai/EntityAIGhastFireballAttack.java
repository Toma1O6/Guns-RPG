package dev.toma.gunsrpg.ai;

import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIGhastFireballAttack extends EntityAIBase {

    private final EntityGhast ghast;
    public int attackTimer;

    public EntityAIGhastFireballAttack(EntityGhast ghast) {
        this.ghast = ghast;
    }

    @Override
    public boolean shouldExecute() {
        return ghast.getAttackTarget() != null;
    }

    @Override
    public void startExecuting() {
        this.attackTimer = 0;
    }

    @Override
    public void resetTask() {
        this.ghast.setAttacking(false);
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = ghast.getAttackTarget();
        double maxRange = 4096.0D;
        if(target.getDistanceSq(ghast) < maxRange && (ghast.canEntityBeSeen(target) || WorldDataFactory.isBloodMoon(ghast.world))) {
            World world = ghast.getEntityWorld();
            ++attackTimer;
            if(attackTimer == 10) {
                world.playEvent(null, 1015, new BlockPos(ghast), 0);
            }
            if(attackTimer == 20) {
                double d = 4.0D;
                Vec3d look = ghast.getLook(1.0F);
                double x = target.posX - (ghast.posX + look.x * d);
                double y = target.getEntityBoundingBox().minY + (target.height / 2.0) - (0.5 + ghast.posY + (ghast.height / 2.0));
                double z = target.posZ - (ghast.posZ + look.z * d);
                world.playEvent(null, 1016, new BlockPos(ghast), 0);
                EntityLargeFireball fireball = new EntityLargeFireball(world, ghast, x, y, z);
                fireball.explosionPower = ghast.getFireballStrength();
                fireball.posX = ghast.posX + look.x * d;
                fireball.posY = ghast.posY + (ghast.height / 2.0) + 0.5;
                fireball.posZ = ghast.posZ + look.z * d;
                world.spawnEntity(fireball);
                attackTimer = -40;
            }
        } else if(attackTimer > 0) {
            attackTimer--;
        }
        ghast.setAttacking(attackTimer > 10);
    }
}
