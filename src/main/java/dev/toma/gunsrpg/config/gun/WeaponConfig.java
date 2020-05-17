package dev.toma.gunsrpg.config.gun;

import toma.config.object.builder.ConfigBuilder;

public class WeaponConfig {

    public WeaponConfiguration pistol = new WeaponConfiguration("Pistol", 3.0F, 14.0F, 3, 0.2F, 0.4F);
    public WeaponConfiguration smg = new WeaponConfiguration("SMG", 2.0F, 16.0F, 5, 0.2F, 0.4F);
    public WeaponConfiguration ar = new WeaponConfiguration("AR", 7.0F, 19.0F, 6, 0.3F, 0.6F);
    public WeaponConfiguration sr = new WeaponConfiguration("SR", 15.0F, 22.0F, 7, 0.6F, 1.0F);
    public WeaponConfiguration shotgun = new WeaponConfiguration("Shotgun", 2.0F, 11.0F, 1, 0.5F, 1.2F);

    public ConfigBuilder toConfigFormat(ConfigBuilder builder) {
        return builder
                .push().name("Weapons").init()
                .run(pistol::build)
                .run(smg::build)
                .run(ar::build)
                .run(sr::build)
                .run(shotgun::build)
                .pop();
    }
}
