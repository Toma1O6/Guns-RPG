package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.LazyLoader;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ZombieNightmareEntity extends MonsterEntity {

    private final LazyLoader<EntityType<?>[]> BUFFED_MOBS = new LazyLoader<>(() -> new EntityType<?>[] {EntityType.ZOMBIE, EntityType.HUSK, EntityType.ZOMBIE_VILLAGER, EntityType.DROWNED, ModEntities.ZOMBIE_KNIGHT.get(), ModEntities.ZOMBIE_GUNNER.get()});
    private float healthLost;

    public ZombieNightmareEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.29D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
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
                ZombieEntity entity = new ZombieEntity(EntityType.ZOMBIE, level);
                entity.setPos(this.getX(), this.getY(), this.getZ());
                level.addFreshEntity(entity);
            }
            if (tickCount % 20 == 0) {
                addEffect(new EffectInstance(Effects.REGENERATION, 40, 1, false, false));
                this.level.getEntities(this, this.getBoundingBox().inflate(20.0D), entity -> {
                    EntityType<?> type = entity.getType();
                    return ModUtils.contains(type, BUFFED_MOBS.get()) && entity instanceof LivingEntity;
                })
                        .stream()
                        .map(entity -> (LivingEntity) entity)
                        .forEach(livingEntity -> {
                            livingEntity.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 40, 0));
                            livingEntity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 40, 0));
                        });
            }
        }
    }

    @Override
    public boolean canBeAffected(EffectInstance effect) {
        return true;
    }

    @Override
    public void aiStep() {
        if (this.isAlive() && isSunBurnTick()) {
            this.setSecondsOnFire(8);
        }
        super.aiStep();
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
}
