package dev.toma.gunsrpg.ai;

import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.entity.ZombieGunnerEntity;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class GunAttackGoal extends Goal {

    protected static final int[] ATTACK_RANGE_TABLE = {10, 15, 20, 25, 10, 25};
    protected final ZombieGunnerEntity entity;
    private final IShootProps props;
    private IShootingHandler handler;

    public GunAttackGoal(ZombieGunnerEntity gunner) {
        this.entity = gunner;
        this.props = new GunnerProjectileProperties(gunner);
        setFlags(EnumSet.of(Flag.TARGET, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.hasGun() || !entity.isLoaded()) {
            return false;
        }
        return entity.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || !this.entity.getNavigation().isDone() || this.hasGun();
    }

    @Override
    public void start() {
        assignHandler();
        handler.reset();
    }

    @Override
    public void stop() {
        handler.reset();
        handler = null;
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        if (target == null || !hasGun()) return;
        double dist = Math.sqrt(entity.distanceToSqr(target));
        ItemStack stack = entity.getMainHandItem();
        GunItem gun = (GunItem) stack.getItem();
        double attackRange = ATTACK_RANGE_TABLE[gun.getWeaponCategory().ordinal()];
        boolean canSee = canSeeEntity(target);
        if (dist <= attackRange && canSee) {
            entity.getNavigation().stop();
        } else entity.getNavigation().moveTo(target, 1.0D);
        this.entity.lookAt(target, 30, 30);
        this.entity.getLookControl().setLookAt(target, 30, 30);
        if (canSee && dist < attackRange * 3 && handler.isReadyForShooting() && !entity.level.isClientSide) {
            if (handler.tryShootingTick()) {
                SoundEvent event = gun.getWeaponShootSound(entity);
                gun.shoot(entity.level, entity, stack, props, event);
                handler.reset();
            }
        }
    }

    private void assignHandler() {
        handler = entity.getBurstDelay() == 0 ? new MagazineBasedHandler(entity) : new BurstShootingHandler(entity);
    }

    private boolean hasGun() {
        return entity.getMainHandItem().getItem() instanceof GunItem;
    }

    private boolean canSeeEntity(LivingEntity target) {
        World world = entity.level;
        BlockRayTraceResult result = ModUtils.raytraceBlocksIgnoreGlass(
                new Vector3d(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ()),
                new Vector3d(target.getX(), target.getY() + target.getEyeHeight(), target.getZ()),
                world
        );
        if (result == null || result.getType() == RayTraceResult.Type.MISS) {
            return true;
        }
        BlockPos pos = result.getBlockPos();
        BlockState state = world.getBlockState(pos);
        VoxelShape shape = state.getCollisionShape(world, pos);
        if (shape.isEmpty()) {
            return true;
        }
        return state.getMaterial().isReplaceable() || state.getMaterial() == Material.GLASS;
    }

    private static class GunnerProjectileProperties implements IShootProps {

        private final ZombieGunnerEntity entity;

        public GunnerProjectileProperties(ZombieGunnerEntity entity) {
            this.entity = entity;
        }

        @Override
        public float getDamageMultiplier() {
            return entity.getDamageMultiplier();
        }

        @Override
        public float getInaccuracy() {
            return entity.getInaccuracy();
        }
    }

    private interface IShootingHandler {

        boolean isReadyForShooting();

        boolean tryShootingTick();

        void reset();
    }

    private static class MagazineBasedHandler implements IShootingHandler {

        private final IntSupplier magCapacity;
        private final IntSupplier reloadCycleLength;
        private final IntSupplier firerate;
        private int ammoLeft;
        private int reloadTime;
        private int fireDelay;

        public MagazineBasedHandler(ZombieGunnerEntity entity) {
            this.magCapacity = entity::getMagCapacity;
            this.reloadCycleLength = entity::getReloadTime;
            this.ammoLeft = this.magCapacity.getInt();
            this.firerate = entity::getFirerate;
        }

        @Override
        public boolean isReadyForShooting() {
            return true;
        }

        @Override
        public void reset() {
            fireDelay = firerate.getInt();
        }

        @Override
        public boolean tryShootingTick() {
            tickReloading();
            if (ammoLeft > 0) {
                if (fireDelay <= 0) {
                    return doShooting();
                } else {
                    --fireDelay;
                }
            } else {
                startReloading();
            }
            return false;
        }

        protected boolean doShooting() {
            --ammoLeft;
            return true;
        }

        protected void tickReloading() {
            if (reloadTime > 0) {
                --reloadTime;
                if (reloadTime == 0) {
                    finishReloading();
                }
            }
        }

        protected void startReloading() {
            if (isReloading()) {
                return;
            }
            reloadTime = reloadCycleLength.getInt();
        }

        protected void finishReloading() {
            ammoLeft = magCapacity.getInt();
        }

        protected boolean isReloading() {
            return reloadTime > 0;
        }
    }

    private static class BurstShootingHandler extends MagazineBasedHandler {

        private final IntSupplier burstDelayTotal;
        private final IntSupplier burstSize;
        private int actualDelay;
        private int shotsFired;

        public BurstShootingHandler(ZombieGunnerEntity entity) {
            super(entity);
            this.burstDelayTotal = entity::getBurstDelay;
            this.burstSize = entity::getBurstSize;
        }

        @Override
        protected void finishReloading() {
            super.finishReloading();
            shotsFired = 0;
        }

        @Override
        protected boolean doShooting() {
            if (actualDelay > 0) {
                --actualDelay;
                shotsFired = 0;
                return false;
            }
            ++shotsFired;
            if (shotsFired >= burstSize.getInt()) {
                actualDelay = burstDelayTotal.getInt();
            }
            return super.doShooting();
        }
    }

    @FunctionalInterface
    private interface IntSupplier {
        int getInt();
    }
}
