package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.ai.EntityAIGunAttack;
import dev.toma.gunsrpg.common.init.GRPGEntityTypes;
import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
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
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class ZombieGunnerEntity extends MonsterEntity {

    // loaded lazily to prevent getting null items
    public static final LazyOptional<List<GunData>> DATA = LazyOptional.of(() -> {
        List<GunData> list = new ArrayList<>();
        list.add(GunData.from(GRPGItems.PISTOL, 30));
        list.add(GunData.from(GRPGItems.SMG, 15));
        list.add(GunData.from(GRPGItems.ASSAULT_RIFLE, 30));
        list.add(GunData.from(GRPGItems.SNIPER_RIFLE, 80));
        list.add(GunData.from(GRPGItems.SHOTGUN, 50));
        return list;
    });
    private int weaponDataIndex;

    public ZombieGunnerEntity(World world) {
        this(GRPGEntityTypes.ZOMBIE_GUNNER.get(), world);
    }

    public ZombieGunnerEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new EntityAIGunAttack(this));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ARMOR, 2.0);
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
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
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
        List<GunData> lootpools = getWeaponDataList();
        weaponDataIndex = random.nextInt(lootpools.size());
        GunData data = lootpools.get(weaponDataIndex);
        setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(data.gun));
        setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("dataIndex", weaponDataIndex);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        weaponDataIndex = compound.getInt("dataIndex");
        List<GunData> list = getWeaponDataList();
        int range = list.size();
        if (weaponDataIndex >= range)
            weaponDataIndex = random.nextInt(range);
    }

    public GunData getWeaponData() {
        List<GunData> list = getWeaponDataList();
        int max = list.size();
        if (weaponDataIndex >= max) {
            weaponDataIndex = random.nextInt(max);
        }
        return list.get(weaponDataIndex);
    }

    private List<GunData> getWeaponDataList() {
        return DATA.orElseThrow(() -> new IllegalStateException("Zombie gunner equipment pool is not populated!"));
    }

    public static class GunData {

        public final GunItem gun;
        public final int rof;
        public final BiFunction<GunItem, LivingEntity, SoundEvent> event;

        public GunData(GunItem gun, int rof, BiFunction<GunItem, LivingEntity, SoundEvent> event) {
            this.gun = gun;
            this.rof = rof;
            this.event = event;
        }

        public static GunData from(GunItem item, int firerate) {
            return new GunData(item, firerate, GunItem::getEntityShootSound);
        }
    }
}
