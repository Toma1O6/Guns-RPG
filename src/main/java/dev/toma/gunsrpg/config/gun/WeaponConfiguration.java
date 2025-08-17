package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.api.common.IJamConfig;
import dev.toma.gunsrpg.api.common.IWeaponConfig;

public class WeaponConfiguration implements IWeaponConfig {

    @Configurable
    @Configurable.Range(min = 0, max = Short.MAX_VALUE)
    @Configurable.Comment("Tick amount for how long projectile is not affected by gravity")
    public int gravityDelay;

    @Configurable
    @Configurable.Range(min = 1, max = 10000)
    @Configurable.Comment("Projectile velocity in m/s")
    public int velocity;

    @Configurable
    @Configurable.DecimalRange(min = 1.0)
    @Configurable.Comment("Projectile damage")
    public float damage;

    @Configurable
    @Configurable.Comment("Weapon jamming settings")
    public IJamConfig jamConfig;

    public WeaponConfiguration(float damage, int velocity, int delay, float jamMin, float jamMax) {
        this.gravityDelay = delay;
        this.damage = damage;
        this.velocity = velocity;
        this.jamConfig = new JamConfig(jamMin, jamMax, IJamConfig.DEFAULT_EASING);
    }

    @Override
    public float getDamage() {
        return damage;
    }

    @Override
    public float getVelocity() {
        return velocity / 20.0F;
    }

    @Override
    public int getGravityDelay() {
        return gravityDelay;
    }

    @Override
    public IJamConfig getJamConfig() {
        return jamConfig;
    }
}
