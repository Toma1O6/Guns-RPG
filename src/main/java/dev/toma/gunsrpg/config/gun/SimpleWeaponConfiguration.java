package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.api.common.IJamConfig;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import lib.toma.animations.Easings;

public final class SimpleWeaponConfiguration implements IWeaponConfig {

    @Configurable
    @Configurable.DecimalRange(min = 1.0, max = 10000.0)
    @Configurable.Comment("Projectile velocity")
    @Configurable.Gui.NumberFormat("0.0###")
    public float velocity;

    @Configurable
    @Configurable.Comment("Weapon jamming settings")
    public IJamConfig jamConfig;

    public SimpleWeaponConfiguration(float velocity, float jamMin, float jamMax) {
        this(velocity, jamMin, jamMax, IJamConfig.DEFAULT_EASING);
    }

    public SimpleWeaponConfiguration(float velocity, float jamMin, float jamMax, Easings easing) {
        this.velocity = velocity;
        this.jamConfig = new JamConfig(jamMin, jamMax, easing);
    }

    @Override
    public float getDamage() {
        return 0.0F;
    }

    @Override
    public float getVelocity() {
        return velocity;
    }

    @Override
    public int getGravityDelay() {
        return 0;
    }

    @Override
    public IJamConfig getJamConfig() {
        return jamConfig;
    }
}
