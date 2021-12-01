package dev.toma.gunsrpg.ai;

import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.entity.ZombieGunnerEntity;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class GunAttackGoal extends Goal {

    protected static final int[] ATTACK_RANGE_TABLE = {10, 15, 20, 25, 10};
    protected final ZombieGunnerEntity entity;
    protected int timeRemaining = 10;
    private final IShootProps props;

    public GunAttackGoal(ZombieGunnerEntity gunner) {
        this.entity = gunner;
        this.props = new GunnerProjectileProperties(gunner);
        setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (!this.hasGun()) {
            return false;
        }
        return entity.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || !this.entity.getNavigation().isDone() || this.hasGun();
    }

    @Override
    public void stop() {
        timeRemaining = 10;
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
        if (canSee && dist < attackRange * 3 && --timeRemaining <= 0 && !entity.level.isClientSide) {
            SoundEvent event = gun.getWeaponShootSound(entity);
            gun.shoot(entity.level, entity, stack, props, event);
            timeRemaining = entity.getFirerate();
        }
    }

    private boolean hasGun() {
        return entity.getMainHandItem().getItem() instanceof GunItem;
    }

    private boolean canSeeEntity(LivingEntity target) {
        return ModUtils.raytraceBlocksIgnoreGlass(
                new Vector3d(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ()),
                new Vector3d(target.getX(), target.getY() + target.getEyeHeight(), target.getZ()),
                entity.level
        ) != null;
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
}
