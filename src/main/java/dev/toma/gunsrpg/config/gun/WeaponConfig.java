package dev.toma.gunsrpg.config.gun;

import toma.config.object.builder.ConfigBuilder;

public class WeaponConfig {

    public GeneralSettings general = new GeneralSettings();
    public WeaponConfiguration pistol = new WeaponConfiguration("Pistol", 4.0F, 14.0F, 3, 1.4F, 3.1F, 4, 3);
    public WeaponConfiguration smg = new WeaponConfiguration("SMG", 3.0F, 16.0F, 5, 0.7F, 1.8F, 3, 2);
    public WeaponConfiguration ar = new WeaponConfiguration("AR", 7.0F, 19.0F, 6, 1.3F, 4.7F, 7, 6);
    public WeaponConfiguration sr = new WeaponConfiguration("SR", 15.0F, 22.0F, 7, 0.9F, 8.0F, 40, 25);
    public WeaponConfiguration shotgun = new WeaponConfiguration("Shotgun", 2.0F, 11.0F, 1, 1.9F, 5.3F, 25, 15);
    public WeaponConfiguration crossbow = new WeaponConfiguration("Crossbow", 10.0F, 3.2F, 3, 0.1F, 0.1F, 10);

    public ConfigBuilder toConfigFormat(ConfigBuilder builder) {
        return builder
                .push().name("Weapons").init()
                .run(general::build)
                .run(pistol::build)
                .run(smg::build)
                .run(ar::build)
                .run(sr::build)
                .run(shotgun::build)
                .run(crossbow::build)
                .pop();
    }
}
