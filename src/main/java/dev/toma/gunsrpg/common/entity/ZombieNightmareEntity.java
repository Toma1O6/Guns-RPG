package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.init.ModPotions;
import dev.toma.gunsrpg.common.init.ModTags;
import dev.toma.gunsrpg.util.Interval;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class ZombieNightmareEntity extends MonsterEntity {

    private float healthLost;
    private boolean running;

    public ZombieNightmareEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.29D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new RecoverSelfGoal(this, 30.0F, 50.0F));
        this.goalSelector.addGoal(2, new NightmareMeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide) {
            while (healthLost >= 30.0F) {
                healthLost =- 30.0F;
                spawnReinforcementMob();
            }
            if (tickCount % 20 == 0) {
                addEffect(new EffectInstance(Effects.REGENERATION, 40, 2, false, false));
                this.level.getEntities(this, this.getBoundingBox().inflate(20.0D), companionFilter())
                        .stream()
                        .map(entity -> (LivingEntity) entity)
                        .forEach(livingEntity -> {
                            livingEntity.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 40, 0));
                            livingEntity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 40, 0));
                            livingEntity.heal(1.0F);
                        });
            }
        }
    }

    @Override
    public boolean canBeAffected(EffectInstance effect) {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    @Override
    public boolean isLeftHanded() {
        return false;
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason spawnReason, @Nullable ILivingEntityData data, @Nullable CompoundNBT compoundNBT) {
        super.finalizeSpawn(world, difficulty, spawnReason, data, compoundNBT);
        populateDefaultEquipmentSlots(difficulty);
        populateDefaultEquipmentEnchantments(difficulty);
        return data;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance diff) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        boolean hurt = super.hurt(damageSource, amount);
        if (hurt) {
            this.healthLost += amount;
        }
        return hurt;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean attacked = super.doHurtTarget(target);
        if (attacked && target instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) target;
            living.addEffect(new EffectInstance(Effects.WEAKNESS, Interval.seconds(10).getTicks(), 0));
            living.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, Interval.seconds(10).getTicks(), 0));
        }
        return attacked;
    }

    protected void spawnReinforcements(int count) {
        for (int i = 0; i < count; i++) {
            spawnReinforcementMob();
        }
    }

    protected void spawnReinforcementMob() {
        if (level.isClientSide)
            return;
        ServerWorld world = (ServerWorld) level;
        ZombieEntity entity = new ZombieEntity(EntityType.ZOMBIE, level);
        entity.setPos(this.getX(), this.getY(), this.getZ());
        entity.finalizeSpawn(world, world.getCurrentDifficultyAt(blockPosition()), SpawnReason.REINFORCEMENT, null, null);
        entity.setTarget(this.getTarget());
        level.addFreshEntity(entity);
    }

    public static Predicate<Entity> companionFilter() {
        return e -> e instanceof LivingEntity && e.getType().is(ModTags.Entities.ZOMBIE_NIGHTMARE_COMPANIONS);
    }

    public void setRunning(boolean running) {
        this.setSprinting(running);
        this.running = running;
    }

    private static final class RecoverSelfGoal extends Goal {

        private final ZombieNightmareEntity nightmareEntity;
        private final float initiateAtHealth;
        private final float stopAtHealth;

        private boolean active;
        private int taskCooldown;
        private LivingEntity lastTarget;

        public RecoverSelfGoal(ZombieNightmareEntity nightmareEntity, float initiateAtHealth, float stopAtHealth) {
            this.nightmareEntity = nightmareEntity;
            this.initiateAtHealth = initiateAtHealth;
            this.stopAtHealth = stopAtHealth;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return !active && nightmareEntity.getHealth() <= initiateAtHealth && nightmareEntity.getLastHurtByMob() != null && taskCooldown <= 0;
        }

        @Override
        public boolean canContinueToUse() {
            return active && nightmareEntity.getHealth() < stopAtHealth && lastTarget != null && lastTarget.isAlive();
        }

        @Override
        public void start() {
            this.active = true;
            this.nightmareEntity.setRunning(true);
            this.nightmareEntity.setTarget(null);
            this.nightmareEntity.navigation.stop();
            this.throwPotionIfApplicable();
            this.runAway();
            this.nightmareEntity.spawnReinforcements(3);
            this.nightmareEntity.level.getEntities(nightmareEntity, nightmareEntity.getBoundingBox().inflate(60), companionFilter())
                    .stream()
                    .filter(entity -> entity instanceof MonsterEntity)
                    .map(entity -> (MonsterEntity) entity)
                    .forEach(entity -> {
                        entity.setTarget(nightmareEntity.getTarget());
                        entity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1, false, true));
                    });
            this.taskCooldown = Interval.minutes(2).getTicks();
            this.lastTarget = this.nightmareEntity.getLastHurtByMob();
        }

        @Override
        public void stop() {
            this.active = false;
            this.nightmareEntity.setRunning(false);
            this.lastTarget = null;
        }

        @Override
        public void tick() {
            if (this.taskCooldown > 0) {
                --this.taskCooldown;
            }
            this.runAway();
        }

        private void runAway() {
            if (lastTarget != null) {
                Path path = nightmareEntity.navigation.getPath();
                if (path == null || !path.isDone() || !path.canReach()) {
                    Vector3d hidePosition = RandomPositionGenerator.getPosAvoid(nightmareEntity, 16, 7, lastTarget.position());
                    if (hidePosition != null && lastTarget.distanceToSqr(hidePosition) > nightmareEntity.distanceToSqr(hidePosition)) {
                        nightmareEntity.getNavigation().moveTo(hidePosition.x, hidePosition.y, hidePosition.z, 1.3F);
                        nightmareEntity.setSprinting(true);
                    } else {
                        nightmareEntity.setSprinting(false);
                    }
                } else {
                    nightmareEntity.navigation.moveTo(path, 1.3F);
                }
            }
        }

        private void throwPotionIfApplicable() {
            if (nightmareEntity.level.isClientSide)
                return;
            LivingEntity attacker = nightmareEntity.getLastHurtByMob();
            if (attacker != null && nightmareEntity.distanceToSqr(attacker) < 64) {
                Vector3d vec = attacker.getDeltaMovement();
                double x = attacker.getX() + vec.x - nightmareEntity.getX();
                double y = attacker.getEyeY() - 1.1F - nightmareEntity.getY();
                double z = attacker.getZ() + vec.z - nightmareEntity.getZ();
                float dist = MathHelper.sqrt(x * x + z * z);
                PotionEntity potionentity = new PotionEntity(nightmareEntity.level, nightmareEntity);
                potionentity.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), ModPotions.ZOMBIE_NIGHTMARE_DEFENSIVE_POTION.get()));
                potionentity.xRot -= -20.0F;
                potionentity.shoot(x, y + dist * 0.2F, z, 0.4F, 8.0F);
                if (!nightmareEntity.isSilent()) {
                    nightmareEntity.level.playSound(null, nightmareEntity.getX(), nightmareEntity.getY(), nightmareEntity.getZ(), SoundEvents.WITCH_THROW, nightmareEntity.getSoundSource(), 1.0F, 0.8F + nightmareEntity.random.nextFloat() * 0.4F);
                }

                nightmareEntity.level.addFreshEntity(potionentity);
            }
        }
    }

    private static final class NightmareMeleeAttackGoal extends MeleeAttackGoal {

        public NightmareMeleeAttackGoal(ZombieNightmareEntity entity, double speed, boolean followHidden) {
            super(entity, speed, followHidden);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !((ZombieNightmareEntity) mob).running;
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !((ZombieNightmareEntity) mob).running;
        }
    }
}
