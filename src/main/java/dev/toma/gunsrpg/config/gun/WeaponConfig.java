package dev.toma.gunsrpg.config.gun;

import net.minecraftforge.common.config.Config;

public class WeaponConfig {

    @Config.Name("Attachment settings")
    public GeneralSettings general = new GeneralSettings();

    @Config.Name("Pistol")
    public WeaponConfiguration pistol = new WeaponConfiguration(4.0F, 14.0F, 3, 1.4F, 3.1F, 4, 3);

    @Config.Name("Sub-machine gun")
    public WeaponConfiguration smg = new WeaponConfiguration(3.0F, 16.0F, 5, 0.7F, 1.8F, 3, 2);

    @Config.Name("Assault rifle")
    public WeaponConfiguration ar = new WeaponConfiguration(7.0F, 19.0F, 6, 1.3F, 4.7F, 7, 6);

    @Config.Name("Sniper rifle")
    public WeaponConfiguration sr = new WeaponConfiguration(15.0F, 22.0F, 7, 0.9F, 8.0F, 40, 25);

    @Config.Name("Shotgun")
    public WeaponConfiguration shotgun = new WeaponConfiguration(2.0F, 11.0F, 1, 1.9F, 5.3F, 25, 15);

    @Config.Name("Crossbow")
    public WeaponConfiguration crossbow = new WeaponConfiguration(10.0F, 3.2F, 3, 0.1F, 0.1F, 10);
}
