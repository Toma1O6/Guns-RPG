package dev.toma.gunsrpg.api.common;

import dev.toma.configuration.api.IObjectSpec;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;

public interface IWeaponConfig {

    float getDamage();

    float getVelocity();

    @Deprecated
    float getVerticalRecoil();

    @Deprecated
    float getHorizontalRecoil();

    int getGravityDelay();

    @Deprecated
    int getFirerate();

    @Deprecated
    int getUpgradedFirerate();

    static WeaponConfiguration configured(IObjectSpec specification, float damage, int velocity, int gravityDelay, float verticalRecoil, float horizontalRecoil, int baseFirerate) {
        return WeaponConfiguration.basic(specification, damage, velocity, gravityDelay, verticalRecoil, horizontalRecoil, baseFirerate);
    }

    static WeaponConfiguration configured(IObjectSpec specification, float damage, int velocity, int gravityDelay, float verticalRecoil, float horizontalRecoil, int baseFirerate, int upgradedFirerate) {
        return WeaponConfiguration.withUpgrade(specification, damage, velocity, gravityDelay, verticalRecoil, horizontalRecoil, baseFirerate, upgradedFirerate);
    }
}
