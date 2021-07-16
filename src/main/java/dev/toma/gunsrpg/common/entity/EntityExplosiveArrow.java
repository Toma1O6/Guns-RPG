package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.init.GRPGEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityExplosiveArrow extends AbstractArrowEntity {

    public int blastSize;

    public EntityExplosiveArrow(EntityType<? extends EntityExplosiveArrow> type, World world) {
        super(type, world);
    }

    public EntityExplosiveArrow(World world, LivingEntity entity, int blastSize) {
        super(GRPGEntityTypes.EXPLOSIVE_ARROW.get(), entity, world);
        this.pickup = PickupStatus.DISALLOWED;
        this.blastSize = blastSize;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        if (!level.isClientSide) {
            level.explode(this, getX(), getY(), getZ(), getOwner() instanceof ExplosiveSkeletonEntity ? 2 : 1, Explosion.Mode.DESTROY);
            remove();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(level.isClientSide) {
            for(int i = 0; i < 4; i++) {
                level.addParticle(ParticleTypes.LARGE_SMOKE, getX(), getY(), getZ(), r(5), r(5), r(5));
                level.addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), r(3), r(3), r(3));
            }
        }
    }

    private double r(int mod) {
        return random.nextDouble() / mod - random.nextDouble() / mod;
    }
}
