package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.common.init.ModDamageSources;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.play.server.SSpawnParticlePacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class Bullet extends AbstractPenetratingProjectile {

    public Bullet(EntityType<? extends Bullet> type, World world) {
        super(type, world);
    }

    public Bullet(EntityType<? extends Bullet> type, World world, LivingEntity owner) {
        super(type, world, owner);
    }

    @Override
    public void preTick() {}

    @Override
    public void postTick() {}

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return false;
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        if (!level.isClientSide) {
            BlockPos pos = result.getBlockPos();
            BlockState state = level.getBlockState(pos);
            Vector3d vec = result.getLocation();
            Vector3i normal = result.getDirection().getNormal();
            float dirX = normal.getX() / 5.0f;
            float dirY = normal.getX() / 5.0f;
            float dirZ = normal.getX() / 5.0f;
            SSpawnParticlePacket packet = new SSpawnParticlePacket(new BlockParticleData(ParticleTypes.BLOCK, state), true, vec.x, vec.y, vec.z, dirX, dirY, dirZ, 0.01F, 3);
            ((ServerWorld) level).players().forEach(player -> player.connection.send(packet));
            remove();
        }
    }

    @Override
    protected void handleEntityCollision(EntityRayTraceResult result) {
        if (!level.isClientSide) {
            Entity entity = result.getEntity();
            float amount = this.getProjectileDamage();
            this.dealDamageToTarget(entity, amount);
            entity.invulnerableTime = 0;
            remove();
        }
    }

    protected void dealDamageToTarget(Entity target, float damage) {
        target.hurt(ModDamageSources.dealWeaponDamage(this.getOwner(), this, this.getWeapon()), damage);
    }
}
