package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.api.common.IWeaponConfig;

public final class WeaponConfig {

    @Configurable
    @Configurable.Comment("Allows you to disable weapon durability loss while shooting")
    public boolean disableWeaponDurability = false;

    @Configurable
    @Configurable.Comment("M1911 Weapon configuration")
    public IWeaponConfig m1911 = new WeaponConfiguration(4.0F, 280, 3, 0.008F, 0.11F);

    @Configurable
    @Configurable.Comment("R45 weapon configuration")
    public IWeaponConfig r45 = new WeaponConfiguration(7.0F, 320, 4, 0.01F, 0.14F);

    @Configurable
    @Configurable.Comment("Desert Eagle weapon configuration")
    public IWeaponConfig desertEagle = new WeaponConfiguration(8.0F, 330, 5, 0.009F, 0.12F);

    @Configurable
    @Configurable.Comment("UMP weapon configuration")
    public IWeaponConfig ump = new WeaponConfiguration(3.0F, 320, 5, 0.006F, 0.08F);

    @Configurable
    @Configurable.Comment("Thompson weapon configuration")
    public IWeaponConfig thompson = new WeaponConfiguration(5.0F, 320, 5, 0.007F, 0.08F);

    @Configurable
    @Configurable.Comment("Vector weapon configuration")
    public IWeaponConfig vector = new WeaponConfiguration(3.0F, 280, 3, 0.004F, 0.03F);

    @Configurable
    @Configurable.Comment("Crossbow weapon configuration")
    public IWeaponConfig crossbow = new WeaponConfiguration(10.0F, 32, 8, 0.02F, 0.17F);

    @Configurable
    @Configurable.Comment("Chukonu weapon configuration")
    public IWeaponConfig chukonu = new WeaponConfiguration(6.0F, 32, 5, 0.008F, 0.01F);

    @Configurable
    @Configurable.Comment("S1897 weapon configuration")
    public IWeaponConfig s1897 = new WeaponConfiguration(2.0F, 220, 1, 0.013F, 0.15F);

    @Configurable
    @Configurable.Comment("S686 weapon configuration")
    public IWeaponConfig s686 = new WeaponConfiguration(2.0F, 220, 1, 0.017F, 0.14F);

    @Configurable
    @Configurable.Comment("S12k weapon configuration")
    public IWeaponConfig s12k = new WeaponConfiguration(2.0F, 220, 1, 0.014F, 0.12F);

    @Configurable
    @Configurable.Comment("AKM weapon configuration")
    public IWeaponConfig akm = new WeaponConfiguration(6.0F, 330, 4, 0.004F, 0.06F);

    @Configurable
    @Configurable.Comment("HK-416 weapon configuration")
    public IWeaponConfig hk416 = new WeaponConfiguration(5.0F, 380, 6, 0.006F, 0.07F);

    @Configurable
    @Configurable.Comment("AUG weapon configuration")
    public IWeaponConfig aug = new WeaponConfiguration(5.0F, 380, 6, 0.005F, 0.06F);

    @Configurable
    @Configurable.Comment("SKS weapon configuration")
    public IWeaponConfig sks = new WeaponConfiguration(7.0F, 380, 6, 0.01F, 0.14F);

    @Configurable
    @Configurable.Comment("VSS weapon configuration")
    public IWeaponConfig vss = new WeaponConfiguration(6.0F, 140, 2, 0.008F, 0.08F);

    @Configurable
    @Configurable.Comment("Mk-14 EBR weapon configuration")
    public IWeaponConfig mk14 = new WeaponConfiguration(9.0F, 440, 7, 0.008F, 0.09F);

    @Configurable
    @Configurable.Comment("Kar98k weapon configuration")
    public IWeaponConfig kar98k = new WeaponConfiguration(14.0F, 440, 7, 0.035F, 0.16F);

    @Configurable
    @Configurable.Comment("Winchester weapon configuration")
    public IWeaponConfig winchester = new WeaponConfiguration(10.0F, 340, 5, 0.02F, 0.15F);

    @Configurable
    @Configurable.Comment("AWM weapon configuration")
    public IWeaponConfig awm = new WeaponConfiguration(18.0F, 480, 8, 0.025F, 0.14F);

    @Configurable
    @Configurable.Comment("Grenade launcher weapon configuration")
    public IWeaponConfig grenadeLauncher = new SimpleWeaponConfiguration(1.6f, 0.02F, 0.12F);

    @Configurable
    @Configurable.Comment("Rocket launcher weapon configuration")
    public IWeaponConfig rocketLauncher = new SimpleWeaponConfiguration(2.0f, 0.02F, 0.13F);
}
