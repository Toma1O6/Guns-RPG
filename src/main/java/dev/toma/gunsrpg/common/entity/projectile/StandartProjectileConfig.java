package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.init.ModDamageSources;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;

public class StandartProjectileConfig extends AbstractProjectileConfig {

    protected static final float DRAG_MULTIPLIER = 0.995F;

    private final ProjectileSettings settings;
    private final GunItem item;
    private final IPenetrationConfig penConfig;

    public StandartProjectileConfig(LivingEntity entity, GunItem item) {
        super(entity);
        this.item = item;
        this.settings = new ProjectileSettings(item.getWeaponConfig());
        this.penConfig = item.createPenetrationConfig();
    }

    @Override
    public ProjectileSettings getSettings() {
        return settings;
    }

    @Override
    public boolean allowBaseTick() {
        return true;
    }

    @Override
    public void tickPre() {
        // no action needed
    }

    @Override
    public void tickPost() {
        Projectile projectile = projectile();
        Vector3d movement = projectile.getDeltaMovement();
        projectile.move(MoverType.SELF, movement);
    }

    @Override
    public void tickSettings(ProjectileSettings settings) {
        Projectile projectile = projectile();
        settings.applyDrag(DRAG_MULTIPLIER);
        if (settings.shouldApplyGravity(projectile)) {
            projectile.applyGravity();
        }
    }

    @Override
    public void hurtTarget(Entity owner, Entity target, float damage) {
        Projectile projectile = projectile();
        ItemStack stack = projectile.getWeapon();
        target.hurt(ModDamageSources.dealWeaponDamage(owner, projectile, stack), damage);
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return item.getNoiseMultiplier(provider);
    }

    @Override
    public double getHeadshotMultiplier(IAttributeProvider provider) {
        return item.getHeadshotMultiplier(provider);
    }

    @Override
    public IPenetrationConfig createPenConfig() {
        return penConfig;
    }

    @Override
    public EnumProjectileType getRenderType() {
        return EnumProjectileType.BULLET;
    }

    protected float getDragMultiplier() {
        return DRAG_MULTIPLIER;
    }
}
