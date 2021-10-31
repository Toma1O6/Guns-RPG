package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.common.init.ModSounds;
import net.minecraft.util.SoundEvent;

public final class ProjectileSettings {

    private final int delay;
    private final boolean supersonic;
    private final float damage;
    private float velocity;

    public ProjectileSettings(IWeaponConfig config) {
        this.damage = config.getDamage();
        this.velocity = config.getVelocity();
        this.delay = config.getGravityDelay();

        this.supersonic = velocity > 10.0F;
    }

    public SoundEvent getPassBySound() {
        return ModSounds.BULLET_WHIZZ;
    }

    public boolean isSuperSonic() {
        return supersonic;
    }

    public float getInitialDamage() {
        return damage;
    }

    public float getActualVelocity() {
        return velocity;
    }

    public boolean shouldApplyGravity(AbstractProjectile projectile) {
        return projectile.tickCount > delay;
    }

    public void applyDrag(float dragValue) {
        this.velocity *= dragValue;
    }
}
