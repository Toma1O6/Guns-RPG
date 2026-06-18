package com.wf.firearms.entity;

import com.wf.firearms.combat.AmmoCaliber;
import com.wf.firearms.combat.AmmoMaterial;
import com.wf.firearms.combat.FirearmDamage;
import com.wf.firearms.combat.ModDamageTypes;
import com.wf.firearms.combat.WeaponClass;
import com.wf.firearms.registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class BulletProjectile extends Projectile {
    private float damage = 5f;
    private AmmoMaterial ammoMaterial = AmmoMaterial.UNKNOWN;
    private AmmoCaliber ammoCaliber = AmmoCaliber.UNKNOWN;
    private WeaponClass weaponClass = WeaponClass.OTHER;
    private String weaponKey = "";
    private boolean pelletDecay;

    public BulletProjectile(EntityType<? extends BulletProjectile> type, Level level) {
        super(type, level);
    }

    public BulletProjectile(
            EntityType<? extends BulletProjectile> type,
            Level level,
            @Nullable LivingEntity shooter,
            float damage,
            AmmoMaterial ammoMaterial,
            AmmoCaliber ammoCaliber,
            WeaponClass weaponClass,
            String weaponKey,
            boolean pelletDecay) {
        super(type, level);
        setOwner(shooter);
        this.damage = damage;
        this.ammoMaterial = ammoMaterial;
        this.ammoCaliber = ammoCaliber;
        this.weaponClass = weaponClass;
        this.weaponKey = weaponKey != null ? weaponKey : "";
        this.pelletDecay = pelletDecay;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void shootFromRotation(
            LivingEntity shooter, float pitch, float yaw, float unused, float velocity, float inaccuracy) {
        float sinYaw = -Mth.sin(yaw * ((float) Math.PI / 180F));
        float cosPitch = Mth.cos(pitch * ((float) Math.PI / 180F));
        float sinPitch = -Mth.sin(pitch * ((float) Math.PI / 180F));
        float cosYaw = Mth.cos(yaw * ((float) Math.PI / 180F));
        shoot(sinYaw * cosPitch, sinPitch, cosYaw * cosPitch, velocity, inaccuracy);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 dir = new Vec3(x, y, z)
                .normalize()
                .add(
                        random.triangle(0.0, inaccuracy),
                        random.triangle(0.0, inaccuracy),
                        random.triangle(0.0, inaccuracy))
                .scale(velocity);
        setDeltaMovement(dir);
        double horiz = dir.horizontalDistance();
        setYRot((float) (Mth.atan2(dir.x, dir.z) * (180F / (float) Math.PI)));
        setXRot((float) (Mth.atan2(dir.y, horiz) * (180F / (float) Math.PI)));
        yRotO = getYRot();
        xRotO = getXRot();
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = getDeltaMovement();
        HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hit.getType() != HitResult.Type.MISS) {
            onHit(hit);
        }
        double x = getX() + motion.x;
        double y = getY() + motion.y;
        double z = getZ() + motion.z;
        setPos(x, y, z);
        if (level().isClientSide) {
            return;
        }
        if (pelletDecay && tickCount > 2) {
            damage *= 0.8f;
        }
        if (tickCount > 80 || level().isOutsideBuildHeight(blockPosition())) {
            discard();
            return;
        }
        setDeltaMovement(motion.scale(0.99));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (level().isClientSide) {
            return;
        }
        if (result.getEntity() instanceof LivingEntity target && getOwner() != null) {
            double dist = getOwner().distanceTo(target);
            float hitDamage = FirearmDamage.withDistanceFalloff(damage, weaponKey, weaponClass, dist);
            if (getOwner() instanceof Player player) {
                hitDamage =
                        com.wf.firearms.gameplay.WeaponExtensionCombat.damageMultiplier(
                                player, weaponKey, target, hitDamage);
            }
            target.hurt(ModDamageTypes.bullet(level(), getOwner()), hitDamage);
            if (getOwner() instanceof Player player) {
                com.wf.firearms.gameplay.WeaponExtensionCombat.onGunHit(player, weaponKey, target);
            }
        }
        discard();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide) {
            discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        damage = tag.getFloat("Damage");
        ammoMaterial = AmmoMaterial.fromId(tag.getString("AmmoMat"));
        try {
            ammoCaliber = AmmoCaliber.valueOf(tag.getString("AmmoCal"));
        } catch (IllegalArgumentException ex) {
            ammoCaliber = AmmoCaliber.UNKNOWN;
        }
        try {
            weaponClass = WeaponClass.valueOf(tag.getString("WeaponClass"));
        } catch (IllegalArgumentException ex) {
            weaponClass = WeaponClass.OTHER;
        }
        pelletDecay = tag.getBoolean("PelletDecay");
        weaponKey = tag.getString("WeaponKey");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Damage", damage);
        tag.putString("AmmoMat", ammoMaterial.getId());
        tag.putString("AmmoCal", ammoCaliber.name());
        tag.putString("WeaponClass", weaponClass.name());
        tag.putBoolean("PelletDecay", pelletDecay);
        tag.putString("WeaponKey", weaponKey);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.BULLET.get();
    }
}
