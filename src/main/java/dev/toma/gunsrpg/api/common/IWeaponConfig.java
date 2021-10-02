package dev.toma.gunsrpg.api.common;

import dev.toma.configuration.api.IObjectSpec;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;

public interface IWeaponConfig {

    float getDamage();

    float getVelocity();

    int getGravityDelay();

    static WeaponConfiguration configured(IObjectSpec specification, float damage, int velocity, int gravityDelay) {
        return WeaponConfiguration.basic(specification, damage, velocity, gravityDelay);
    }
}
