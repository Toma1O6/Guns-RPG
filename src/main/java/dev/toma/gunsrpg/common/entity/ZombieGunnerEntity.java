package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ai.GunAttackGoal;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.resource.gunner.ZombieGunnerLoadout;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

public class ZombieGunnerEntity extends MonsterEntity {

    private float damageMultiplier = 1.0F;
    private float inaccuracy = 0.15F;
    private int firerate = 20;
    private int magCapacity = 10;
    private int reloadTime = 40;

    public ZombieGunnerEntity(World world) {
        this(ModEntities.ZOMBIE_GUNNER.get(), world);
    }

    public ZombieGunnerEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ARMOR, 2.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new GunAttackGoal(this));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    public void aiStep() {
        if (isAlive()) {
            boolean flag = isSunBurnTick();
            if (flag) {
                ItemStack stack = getItemBySlot(EquipmentSlotType.HEAD);
                if (!stack.isEmpty()) {
                    if (stack.isDamageableItem()) {
                        stack.setDamageValue(stack.getDamageValue() + random.nextInt(2));
                        if (stack.getDamageValue() >= stack.getMaxDamage()) {
                            broadcastBreakEvent(EquipmentSlotType.HEAD);
                            setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                        }
                    }
                    flag = false;
                }
                if (flag) {
                    setSecondsOnFire(8);
                }
            }
        }
        super.aiStep();
    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
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
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData data, CompoundNBT nbt) {
        data = super.finalizeSpawn(serverWorld, difficulty, reason, data, nbt);
        populateDefaultEquipmentSlots(difficulty);
        return data;
    }

    @Override
    protected void dropAllDeathLoot(DamageSource p_213345_1_) {
        super.dropAllDeathLoot(p_213345_1_);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource p_213333_1_, int p_213333_2_, boolean p_213333_3_) {
    }

    @Override
    protected void populateDefaultEquipmentEnchantments(DifficultyInstance p_180483_1_) {
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(difficulty);
        ZombieGunnerLoadout loadout = GunsRPG.getModLifecycle().getZombieGunnerWeaponManager().getLoadout();
        loadout.applyGear(this, difficulty.getDifficulty());
        setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("damageMultiplier", damageMultiplier);
        compound.putFloat("inaccuracy", inaccuracy);
        compound.putInt("magCapacity", magCapacity);
        compound.putInt("firerate", firerate);
        compound.putInt("reloadTime", reloadTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        damageMultiplier = compound.getFloat("damageMultiplier");
        inaccuracy = compound.getFloat("inaccuracy");
        magCapacity = compound.getInt("magCapacity");
        firerate = compound.getInt("firerate");
        reloadTime = compound.getInt("reloadTime");
    }

    public void setFirerate(int firerate) {
        this.firerate = firerate;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getInaccuracy() {
        return inaccuracy;
    }

    public int getFirerate() {
        return firerate;
    }

    public int getMagCapacity() {
        return magCapacity;
    }

    public int getReloadTime() {
        return reloadTime;
    }
}
