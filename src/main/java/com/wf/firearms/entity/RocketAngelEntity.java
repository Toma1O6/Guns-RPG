package com.wf.firearms.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import com.wf.firearms.config.BloodmoonConfig;
import com.wf.firearms.config.MobSpawnConfig;
import com.wf.firearms.config.MobSpawnConfig.MobCombat;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RocketAngelEntity extends Monster {
    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(RocketAngelEntity.class, EntityDataSerializers.INT);

    public RocketAngelEntity(EntityType<? extends RocketAngelEntity> type, Level level) {
        super(type, level);
        xpReward = 8;
        moveControl = new AngelMoveControl(this);
        if (!level.isClientSide) {
            setVariant(Type.random(level.random));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 45.0)
                .add(Attributes.ATTACK_DAMAGE, 15.0)
                .add(Attributes.FOLLOW_RANGE, 96.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_VARIANT, 0);
    }

    @Override
    public void tick() {
        super.tick();
        setNoGravity(true);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(4, new RocketAttackGoal(this));
        goalSelector.addGoal(8, new RandomFlyGoal());
        goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0f, 1.0f));
        goalSelector.addGoal(10, new LookAtPlayerGoal(this, LivingEntity.class, 8.0f));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public int getTextureIndex() {
        return getVariant().ordinal();
    }

    private Type getVariant() {
        int idx = Mth.clamp(entityData.get(DATA_VARIANT), 0, Type.values().length - 1);
        return Type.values()[idx];
    }

    private void setVariant(Type type) {
        entityData.set(DATA_VARIANT, type.ordinal());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", entityData.get(DATA_VARIANT));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Variant")) {
            entityData.set(DATA_VARIANT, tag.getInt("Variant"));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        float damage = (float) getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean hit = target.hurt(damageSources().mobAttack(this), damage);
        if (hit) {
            Vec3 look = getLookAngle();
            Vec3 knock = look.scale(3);
            target.setDeltaMovement(knock.x, Math.abs(knock.y), knock.z);
        }
        return hit;
    }

    private void fireRocketAt(LivingEntity target) {
        MobCombat combat = MobSpawnConfig.rocketAngelCombat();
        Vec3 to = target.getEyePosition().subtract(getEyePosition());
        float spread = combat.rocketSpread();
        to = to.add(
                        (random.nextDouble() - 0.5) * spread,
                        (random.nextDouble() - 0.5) * spread * 0.6,
                        (random.nextDouble() - 0.5) * spread)
                .normalize();
        SmallFireball ball = new SmallFireball(level(), this, to.x, to.y, to.z);
        ball.moveTo(getX(), getY() + 0.5, getZ());
        level().addFreshEntity(ball);
        Type type = getVariant();
        if (type.explodes()) {
            float power = computeExplosionPower(type);
            level().explode(this, target.getX(), target.getY(), target.getZ(), power, ExplosionInteraction.MOB);
        }
        if (type == Type.TOXIN || type == Type.STUN) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
        }
        if (type == Type.STUN) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 3));
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60));
        }
        if (type == Type.INCENDIARY) {
            target.setSecondsOnFire(6);
            if (level().random.nextFloat() < 0.35f) {
                BlockPos firePos = target.blockPosition();
                if (level().getBlockState(firePos).isAir()) {
                    level().setBlockAndUpdate(firePos, net.minecraft.world.level.block.Blocks.FIRE.defaultBlockState());
                }
            }
        }
        playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
    }

    /** 爆炸强度 = 近战攻击 × 配置系数 × 变体系数 × 区域难度；随属性与难度缩放。 */
    private float computeExplosionPower(Type type) {
        float melee = (float) getAttributeValue(Attributes.ATTACK_DAMAGE);
        DifficultyInstance diff = level().getCurrentDifficultyAt(blockPosition());
        float diffFactor = 0.75f + diff.getEffectiveDifficulty() * 0.25f;
        float power = melee * BloodmoonConfig.rocketExplosionMeleeRatio() * type.explosionScale * diffFactor;
        return Mth.clamp(power, 1.0f, 32.0f);
    }

    enum Type {
        STANDARD(35, 1.0f, true),
        HE(10, 1.5f, true),
        INCENDIARY(8, 1.0f, true), // Napalm：引燃目标与落点
        TOXIN(16, 0.85f, true),
        STUN(12, 0.85f, true);

        private final int weight;
        /** 相对标准爆炸的倍率（在近战×难度基础上乘算）。 */
        private final float explosionScale;
        private final boolean explodes;

        Type(int weight, float explosionScale, boolean explodes) {
            this.weight = weight;
            this.explosionScale = explosionScale;
            this.explodes = explodes;
        }

        boolean explodes() {
            return explodes;
        }

        static Type random(net.minecraft.util.RandomSource random) {
            int total = 0;
            for (Type t : values()) {
                total += t.weight;
            }
            int roll = random.nextInt(total);
            int acc = 0;
            for (Type t : values()) {
                acc += t.weight;
                if (roll < acc) {
                    return t;
                }
            }
            return STANDARD;
        }
    }

    private class RandomFlyGoal extends Goal {
        RandomFlyGoal() {
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !getMoveControl().hasWanted() && random.nextInt(7) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            BlockPos origin = blockPosition();
            for (int i = 0; i < 3; i++) {
                BlockPos spot =
                        origin.offset(
                                random.nextInt(15) - 7,
                                random.nextInt(11) - 5,
                                random.nextInt(15) - 7);
                if (level().isEmptyBlock(spot)) {
                    moveControl.setWantedPosition(spot.getX() + 0.5, spot.getY() + 0.5, spot.getZ() + 0.5, 0.25);
                    if (getTarget() == null) {
                        getLookControl()
                                .setLookAt(spot.getX() + 0.5, spot.getY() + 0.5, spot.getZ() + 0.5, 180f, 20f);
                    }
                    break;
                }
            }
        }
    }

    private static class RocketAttackGoal extends Goal {
        private final RocketAngelEntity entity;
        private int shotsLeft;
        private int cooldown;

        RocketAttackGoal(RocketAngelEntity entity) {
            this.entity = entity;
            setFlags(EnumSet.of(Flag.TARGET, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = entity.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void start() {
            MobCombat combat = MobSpawnConfig.rocketAngelCombat();
            shotsLeft = combat.rocketsPerSalvo();
            cooldown = 15;
        }

        @Override
        public void stop() {
            cooldown = Integer.MAX_VALUE;
        }

        @Override
        public void tick() {
            --cooldown;
            LivingEntity target = entity.getTarget();
            if (target == null) {
                return;
            }
            double distSqr = entity.distanceToSqr(target);
            boolean canSee = entity.getSensing().hasLineOfSight(target);
            if (!canSee) {
                entity.getNavigation().stop();
                entity.moveControl.setWantedPosition(target.getX(), target.getY(), target.getZ(), 0.4);
                return;
            }
            if (distSqr < 4.0) {
                if (cooldown <= 0) {
                    cooldown = 10;
                    entity.doHurtTarget(target);
                }
            } else {
                entity.lookAt(target, 30f, 30f);
                entity.getLookControl().setLookAt(target, 30f, 30f);
                if (entity.getY() - target.getY() < 10) {
                    Vec3 mv = entity.getDeltaMovement();
                    entity.setDeltaMovement(mv.x, 0.2, mv.z);
                }
                double follow = entity.getAttributeValue(Attributes.FOLLOW_RANGE);
                if (follow <= 0) {
                    follow = 16;
                }
                if (distSqr < follow * follow / 1.1) {
                    if (cooldown <= 0) {
                        cooldown = 6 + entity.level().getDifficulty().getId();
                        entity.fireRocketAt(target);
                        --shotsLeft;
                        if (shotsLeft <= 0) {
                            MobCombat combat = MobSpawnConfig.rocketAngelCombat();
                            shotsLeft = combat.rocketsPerSalvo();
                            cooldown = combat.salvoReloadTicks();
                        }
                    }
                } else {
                    entity.getNavigation().stop();
                    entity.moveControl.setWantedPosition(target.getX(), target.getY(), target.getZ(), 0.4);
                }
            }
        }
    }

    private static class AngelMoveControl extends MoveControl {
        AngelMoveControl(RocketAngelEntity mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (operation != Operation.MOVE_TO) {
                return;
            }
            RocketAngelEntity angel = (RocketAngelEntity) mob;
            double dx = wantedX - angel.getX();
            double dy = wantedY - angel.getY();
            double dz = wantedZ - angel.getZ();
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist < angel.getBoundingBox().getSize()) {
                operation = Operation.WAIT;
                angel.setDeltaMovement(angel.getDeltaMovement().scale(0.5));
            } else {
                angel.setDeltaMovement(
                        angel.getDeltaMovement()
                                .add(
                                        dx / dist * 0.05 * speedModifier,
                                        dy / dist * 0.05 * speedModifier,
                                        dz / dist * 0.05 * speedModifier));
                if (angel.getTarget() == null) {
                    Vec3 mv = angel.getDeltaMovement();
                    angel.setYRot((float) (-Mth.atan2(mv.x, mv.z) * (180f / (float) Math.PI)));
                } else {
                    double px = angel.getTarget().getX() - angel.getX();
                    double pz = angel.getTarget().getZ() - angel.getZ();
                    angel.setYRot((float) (-Mth.atan2(px, pz) * (180f / (float) Math.PI)));
                }
                angel.yBodyRot = angel.getYRot();
            }
        }
    }
}
