package dev.toma.gunsrpg.config.gun;

import toma.config.object.builder.ConfigBuilder;

public class WeaponConfig {

    public GeneralSettings general = new GeneralSettings();
    public WeaponConfiguration pistol = new WeaponConfiguration("Pistol", 4.0F, 14.0F, 3, 1.2F, 2.3F);
    public WeaponConfiguration smg = new WeaponConfiguration("SMG", 3.0F, 16.0F, 5, 0.7F, 1.6F);
    public WeaponConfiguration ar = new WeaponConfiguration("AR", 7.0F, 19.0F, 6, 1.4F, 2.9F);
    public WeaponConfiguration sr = new WeaponConfiguration("SR", 15.0F, 22.0F, 7, 0.9F, 3.0F);
    public WeaponConfiguration shotgun = new WeaponConfiguration("Shotgun", 2.0F, 11.0F, 1, 0.9F, 3.5F);

    public ConfigBuilder toConfigFormat(ConfigBuilder builder) {
        return builder
                .push().name("Weapons").init()
                .run(general::build)
                .run(pistol::build)
                .run(smg::build)
                .run(ar::build)
                .run(sr::build)
                .run(shotgun::build)
                .pop();
    }
}
