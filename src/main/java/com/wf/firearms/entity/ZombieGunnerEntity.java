package com.wf.firearms.entity;

import com.wf.firearms.ai.GunShootGoal;
import com.wf.firearms.combat.AmmoMaterial;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.WeaponClass;
import com.wf.firearms.mob.AmmoCapColors;
import com.wf.firearms.mob.GunnerLoadoutPicker;
import com.wf.firearms.mob.GunMobDifficulty;
import com.wf.firearms.mob.GunnerLoadoutPicker.PickedLoadout;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class ZombieGunnerEntity extends Monster {
    private static final EntityDataAccessor<String> DATA_WEAPON =
            SynchedEntityData.defineId(ZombieGunnerEntity.class, EntityDataSerializers.STRING);

    private float damageMultiplier = 0.5f;
    private float inaccuracy = 0.12f;
    private int fireIntervalTicks = 12;
    private AmmoMaterial ammoMaterial = AmmoMaterial.IRON;
    private boolean loadoutReady;
    private boolean reloading;

    public ZombieGunnerEntity(EntityType<? extends ZombieGunnerEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ARMOR, 2.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_WEAPON, "m1911");
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new GunShootGoal(this));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8f));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
    }

    @Override
    public void aiStep() {
        if (isAlive() && !level().isClientSide) {
            boolean burn = isSunBurnTick();
            if (burn) {
                ItemStack head = getItemBySlot(EquipmentSlot.HEAD);
                if (!head.isEmpty() && head.isDamageableItem()) {
                    head.hurtAndBreak(1, this, e -> e.broadcastBreakEvent(EquipmentSlot.HEAD));
                    burn = false;
                }
                if (burn) {
                    setSecondsOnFire(8);
                }
            }
        }
        super.aiStep();
    }

    @Override
    @Nullable
    public net.minecraft.world.entity.SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType reason,
            @Nullable net.minecraft.world.entity.SpawnGroupData data,
            @Nullable CompoundTag tag) {
        net.minecraft.world.entity.SpawnGroupData spawnData =
                super.finalizeSpawn(level, difficulty, reason, data, tag);
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            applyLoadout(serverLevel, difficulty);
        }
        return spawnData;
    }

    public void applyLoadout(ServerLevel level, DifficultyInstance difficulty) {
        GunMobDifficulty.Context ctx =
                GunMobDifficulty.evaluate(
                        level,
                        blockPosition(),
                        level.dimension().location(),
                        com.wf.firearms.config.MobSpawnConfig.mob("gunsrpg:zombie_gunner") != null
                                ? com.wf.firearms.config.MobSpawnConfig.mob("gunsrpg:zombie_gunner").spawnStartDay()
                                : 30);
        PickedLoadout picked = GunnerLoadoutPicker.pick(level.random, ctx.combinedDifficulty(), ctx.gunLevelCap());
        setWeaponKey(picked.gunKey());
        ammoMaterial = picked.ammo();
        damageMultiplier = picked.damageMultiplier();
        fireIntervalTicks = picked.fireIntervalTicks();
        inaccuracy = com.wf.firearms.config.GunnerLoadoutConfig.inaccuracy();
        ItemStack gun = GunnerLoadoutPicker.gunStack(picked.gunKey());
        if (!gun.isEmpty()) {
            setItemSlot(EquipmentSlot.MAINHAND, gun);
            setDropChance(EquipmentSlot.MAINHAND, 0f);
        }
        setItemSlot(EquipmentSlot.HEAD, AmmoCapColors.leatherCap(ammoMaterial));
        setDropChance(EquipmentSlot.HEAD, 0f);
        setItemSlot(EquipmentSlot.CHEST, AmmoCapColors.leatherShirt(ammoMaterial));
        setDropChance(EquipmentSlot.CHEST, 0f);
        loadoutReady = true;
    }

    public boolean isReloading() {
        return reloading;
    }

    public void setReloading(boolean reloading) {
        this.reloading = reloading;
    }

    public boolean isLoadoutReady() {
        return loadoutReady;
    }

    public String getWeaponKey() {
        return entityData.get(DATA_WEAPON);
    }

    public void setWeaponKey(String key) {
        entityData.set(DATA_WEAPON, key == null ? "m1911" : key);
    }

    public AmmoMaterial getAmmoMaterial() {
        return ammoMaterial;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getInaccuracy() {
        return inaccuracy;
    }

    public int getFireIntervalTicks() {
        return fireIntervalTicks;
    }

    /** 客户端/逻辑：是否进入持枪瞄准姿势（主手枪械已同步即可，不依赖 loadoutReady）。 */
    public boolean shouldHoldGunPose() {
        return !getMainHandItem().isEmpty();
    }

    public boolean holdsTwoHandedWeapon() {
        WeaponClass wc = FirearmRegistry.classForWeaponKey(getWeaponKey());
        return wc == WeaponClass.RIFLE
                || wc == WeaponClass.SMG
                || wc == WeaponClass.DMR
                || wc == WeaponClass.SNIPER
                || wc == WeaponClass.SHOTGUN
                || wc == WeaponClass.HEAVY
                || wc == WeaponClass.LAUNCHER;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        // 不掉枪
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("WeaponKey", getWeaponKey());
        tag.putString("AmmoMaterial", ammoMaterial.getId());
        tag.putFloat("DamageMultiplier", damageMultiplier);
        tag.putFloat("Inaccuracy", inaccuracy);
        tag.putInt("FireInterval", fireIntervalTicks);
        tag.putBoolean("LoadoutReady", loadoutReady);
        tag.putBoolean("Reloading", reloading);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("WeaponKey")) {
            setWeaponKey(tag.getString("WeaponKey"));
        }
        if (tag.contains("AmmoMaterial")) {
            ammoMaterial = AmmoMaterial.fromId(tag.getString("AmmoMaterial"));
        }
        if (tag.contains("DamageMultiplier")) {
            damageMultiplier = tag.getFloat("DamageMultiplier");
        }
        if (tag.contains("Inaccuracy")) {
            inaccuracy = tag.getFloat("Inaccuracy");
        }
        if (tag.contains("FireInterval")) {
            fireIntervalTicks = tag.getInt("FireInterval");
        }
        loadoutReady = tag.getBoolean("LoadoutReady");
        reloading = tag.getBoolean("Reloading");
    }
}
