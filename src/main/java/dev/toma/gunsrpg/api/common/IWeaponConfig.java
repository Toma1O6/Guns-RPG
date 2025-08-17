package dev.toma.gunsrpg.api.common;

public interface IWeaponConfig {

    float getDamage();

    float getVelocity();

    int getGravityDelay();

    IJamConfig getJamConfig();
}
