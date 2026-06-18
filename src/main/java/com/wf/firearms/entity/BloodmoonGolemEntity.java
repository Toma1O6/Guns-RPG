package com.wf.firearms.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BloodmoonGolemEntity extends Monster {
    private int attackTimer;

    public BloodmoonGolemEntity(EntityType<? extends BloodmoonGolemEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 250.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 35.0)
                .add(Attributes.ARMOR, 10.0)
                .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new GolemMeleeAttackGoal(this, 1.0, true));
        goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(2, new HurtByTargetGoal(this));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (attackTimer > 0) {
            --attackTimer;
        }
        if (getDeltaMovement().horizontalDistanceSqr() > 2.5E-7 && random.nextInt(5) == 0) {
            int i = Mth.floor(getX());
            int j = Mth.floor(getY() - 0.2);
            int k = Mth.floor(getZ());
            BlockPos pos = new BlockPos(i, j, k);
            BlockState state = level().getBlockState(pos);
            if (!level().isEmptyBlock(pos)) {
                level()
                        .addParticle(
                                new BlockParticleOption(ParticleTypes.BLOCK, state),
                                getX() + (random.nextFloat() - 0.5) * getBbWidth(),
                                getY() + 0.1,
                                getZ() + (random.nextFloat() - 0.5) * getBbWidth(),
                                4.0 * (random.nextFloat() - 0.5),
                                0.5,
                                (random.nextFloat() - 0.5) * 4.0);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        attackTimer = 10;
        level().broadcastEntityEvent(this, (byte) 4);
        float damage = (float) getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean hit = target.hurt(damageSources().mobAttack(this), damage);
        if (hit) {
            target.setDeltaMovement(target.getDeltaMovement().add(0.0, 0.4, 0.0));
        }
        playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0f, 1.0f);
        return hit;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            attackTimer = 10;
            playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0f, 1.0f);
        } else {
            super.handleEntityEvent(id);
        }
    }

    public int getAttackTimer() {
        return attackTimer;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.IRON_GOLEM_STEP, 1.0f, 1.0f);
    }

    private static class GolemMeleeAttackGoal extends MeleeAttackGoal {
        GolemMeleeAttackGoal(BloodmoonGolemEntity mob, double speed, boolean longMemory) {
            super(mob, speed, longMemory);
        }

        @Override
        protected double getAttackReachSqr(LivingEntity target) {
            return super.getAttackReachSqr(target) + 0.6;
        }
    }
}
