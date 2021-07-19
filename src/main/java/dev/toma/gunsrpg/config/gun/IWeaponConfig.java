package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.api.IObjectSpec;

public interface IWeaponConfig {

    float getDamage();

    float getVelocity();

    float getVerticalRecoil();

    float getHorizontalRecoil();

    int getGravityDelay();

    int getFirerate();

    int getUpgradedFirerate();

    static WeaponConfiguration configured(IObjectSpec specification, float damage, int velocity, int gravityDelay, float verticalRecoil, float horizontalRecoil, int baseFirerate) {
        return WeaponConfiguration.basic(specification, damage, velocity, gravityDelay, verticalRecoil, horizontalRecoil, baseFirerate);
    }

    static WeaponConfiguration configured(IObjectSpec specification, float damage, int velocity, int gravityDelay, float verticalRecoil, float horizontalRecoil, int baseFirerate, int upgradedFirerate) {
        return WeaponConfiguration.withUpgrade(specification, damage, velocity, gravityDelay, verticalRecoil, horizontalRecoil, baseFirerate, upgradedFirerate);
    }
}
