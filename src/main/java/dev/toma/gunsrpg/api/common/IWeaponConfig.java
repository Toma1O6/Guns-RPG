package dev.toma.gunsrpg.api.common;

import dev.toma.gunsrpg.config.gun.RecoilConfiguration;

public interface IWeaponConfig {

    float getRecoilAnimationScale();

    RecoilConfiguration recoil();

    float getDamage();

    float getVelocity();

    int getGravityDelay();

    IJamConfig getJamConfig();
}
