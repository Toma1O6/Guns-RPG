package dev.toma.gunsrpg.config.gun;

public interface IWeaponConfiguration {

    float getDamage();

    float getVelocity();

    float getVerticalRecoil();

    float getHorizontalRecoil();

    int getGravityDelay();

    int getFirerate();

    int getUpgradedFirerate();
}
