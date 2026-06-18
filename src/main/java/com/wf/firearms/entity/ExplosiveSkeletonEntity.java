package com.wf.firearms.entity;

import com.wf.firearms.ai.LauncherShootGoal;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.config.MobSpawnConfig;
import com.wf.firearms.registry.ModItems;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/** 掷弹手（Grenadier）：红甲骷髅 + 榴弹发射器，多种榴弹轮换射击。 */
public class ExplosiveSkeletonEntity extends Monster {
    private static final EntityDataAccessor<String> DATA_SHELL =
            SynchedEntityData.defineId(ExplosiveSkeletonEntity.class, EntityDataSerializers.STRING);

    private static final List<Supplier<Item>> GRENADE_SHELLS =
            List.of(
                    ModItems.GRENADE_LAUNCHER_SHELL,
                    ModItems.IMPACT_GRENADE_LAUNCHER_SHELL,
                    ModItems.HIGH_EXPLOSIVE_GRENADE_LAUNCHER_SHELL,
                    ModItems.EXPLOSIVE_GRENADE_LAUNCHER_SHELL,
                    ModItems.STICKY_GRENADE_LAUNCHER_SHELL,
                    ModItems.TEAR_GAS_GRENADE_LAUNCHER_SHELL);

    private float damageMultiplier = 0.55f;
    private float inaccuracy = 0.1f;
    private int fireIntervalTicks = 28;
    private boolean loadoutReady;

    public ExplosiveSkeletonEntity(EntityType<? extends ExplosiveSkeletonEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_SHELL, "gunsrpg:grenade_launcher_shell");
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new LauncherShootGoal(this));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8f));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
    }

    @Override
    public void aiStep() {
        if (!level().isClientSide() && !loadoutReady && level() instanceof ServerLevel serverLevel) {
            applyLoadout(serverLevel);
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
            applyLoadout(serverLevel);
        }
        return spawnData;
    }

    public void applyLoadout(ServerLevel level) {
        if (loadoutReady) {
            return;
        }
        Item shell = GRENADE_SHELLS.get(level.random.nextInt(GRENADE_SHELLS.size())).get();
        setPreferredShell(shell);
        damageMultiplier = 0.45f + level.random.nextFloat() * 0.25f;
        inaccuracy = 0.08f + level.random.nextFloat() * 0.06f;
        var spec = FirearmRegistry.getOrDefault("grenade_launcher");
        fireIntervalTicks =
                Math.max(
                        12,
                        Math.round(
                                spec.fireIntervalTicks()
                                        * MobSpawnConfig.global().fireIntervalMultiplier()
                                        * 1.15f));
        ItemStack gun = new ItemStack(ModItems.GRENADE_LAUNCHER.get());
        setItemSlot(EquipmentSlot.MAINHAND, gun);
        setDropChance(EquipmentSlot.MAINHAND, 0f);
        ItemStack chest = new ItemStack(Items.LEATHER_CHESTPLATE);
        chest.getOrCreateTag().putInt("Color", DyeColor.RED.getFireworkColor());
        setItemSlot(EquipmentSlot.CHEST, chest);
        setDropChance(EquipmentSlot.CHEST, 0f);
        loadoutReady = true;
    }

    public boolean isLoadoutReady() {
        return loadoutReady;
    }

    public String getWeaponKey() {
        return "grenade_launcher";
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

    public boolean shouldHoldGunPose() {
        return !getMainHandItem().isEmpty();
    }

    public boolean holdsTwoHandedWeapon() {
        return true;
    }

    public Item pickShellForShot() {
        if (level().random.nextFloat() < 0.35f) {
            setPreferredShell(GRENADE_SHELLS.get(level().random.nextInt(GRENADE_SHELLS.size())).get());
        }
        return resolveShellItem(getPreferredShellId());
    }

    private void setPreferredShell(Item shell) {
        var id = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(shell);
        entityData.set(DATA_SHELL, id != null ? id.toString() : "gunsrpg:grenade_launcher_shell");
    }

    private String getPreferredShellId() {
        return entityData.get(DATA_SHELL);
    }

    private static Item resolveShellItem(String id) {
        if (id == null || id.isEmpty()) {
            return ModItems.GRENADE_LAUNCHER_SHELL.get();
        }
        var loc =
                net.minecraft.resources.ResourceLocation.tryParse(
                        id.contains(":") ? id : "gunsrpg:" + id);
        if (loc == null) {
            return ModItems.GRENADE_LAUNCHER_SHELL.get();
        }
        Item item = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(loc);
        return item != null ? item : ModItems.GRENADE_LAUNCHER_SHELL.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        // 不掉枪
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("PreferredShell", getPreferredShellId());
        tag.putFloat("DamageMultiplier", damageMultiplier);
        tag.putFloat("Inaccuracy", inaccuracy);
        tag.putInt("FireInterval", fireIntervalTicks);
        tag.putBoolean("LoadoutReady", loadoutReady);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("PreferredShell")) {
            entityData.set(DATA_SHELL, tag.getString("PreferredShell"));
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
    }
}
