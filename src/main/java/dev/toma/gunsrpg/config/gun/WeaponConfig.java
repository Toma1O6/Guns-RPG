package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.ObjectType;
import dev.toma.gunsrpg.api.common.IWeaponConfig;

public class WeaponConfig extends ObjectType {

    public final GeneralSettings general;
    public final IWeaponConfig m1911;
    public final IWeaponConfig r45;
    public final IWeaponConfig desertEagle;
    public final IWeaponConfig ump;
    public final IWeaponConfig thompson;
    public final IWeaponConfig vector;
    public final IWeaponConfig crossbow;
    public final IWeaponConfig chukonu;
    public final IWeaponConfig s1897;
    public final IWeaponConfig s686;
    public final IWeaponConfig s12k;
    public final IWeaponConfig akm;
    public final IWeaponConfig hk416;
    public final IWeaponConfig aug;
    public final IWeaponConfig sks;
    public final IWeaponConfig vss;
    public final IWeaponConfig mk14;
    public final IWeaponConfig kar98k;
    public final IWeaponConfig winchester;
    public final IWeaponConfig awm;
    public final IWeaponConfig grenadeLauncher;
    public final IWeaponConfig rocketLauncher;

    public WeaponConfig(IObjectSpec objectSpecification) {
        super(objectSpecification);
        IConfigWriter writer = objectSpecification.getWriter();
        general = writer.writeObject(GeneralSettings::new, "Attachment settings");
        m1911 = writer.writeObject(specification -> new WeaponConfiguration(specification, 4.0F, 280, 3, 0.008F, 0.11F), "M1911");
        r45 = writer.writeObject(specification -> new WeaponConfiguration(specification, 7.0F, 320, 4, 0.01F, 0.14F), "R45");
        desertEagle = writer.writeObject(specification -> new WeaponConfiguration(specification, 8.0F, 330, 5, 0.009F, 0.12F), "Desert Eagle");
        ump = writer.writeObject(specification -> new WeaponConfiguration(specification, 3.0F, 320, 5, 0.006F, 0.08F), "UMP-45");
        thompson = writer.writeObject(specification -> new WeaponConfiguration(specification, 5.0F, 320, 5, 0.007F, 0.08F), "Thompson");
        vector = writer.writeObject(specification -> new WeaponConfiguration(specification, 3.0F, 280, 3, 0.009F, 0.07F), "Vector");
        crossbow = writer.writeObject(specification -> new WeaponConfiguration(specification, 10.0F, 32, 8, 0.02F, 0.17F), "Crossbow");
        chukonu = writer.writeObject(specification -> new WeaponConfiguration(specification, 6.0F, 32, 5, 0.008F, 0.01F), "Chu-Ko-Nu");
        s1897 = writer.writeObject(specification -> new WeaponConfiguration(specification, 2.0F, 220, 1, 0.001F, 0.15F), "S1897");
        s686 = writer.writeObject(specification -> new WeaponConfiguration(specification, 2.0F, 220, 1, 0.017F, 0.14F), "S686");
        s12k = writer.writeObject(specification -> new WeaponConfiguration(specification, 2.0F, 220, 1, 0.01F, 0.14F), "S12k");
        akm = writer.writeObject(specification -> new WeaponConfiguration(specification, 6.0F, 330, 4, 0.004F, 0.06F), "AKM");
        hk416 = writer.writeObject(specification -> new WeaponConfiguration(specification, 5.0F, 380, 6, 0.006F, 0.09F), "HK 416");
        aug = writer.writeObject(specification -> new WeaponConfiguration(specification, 5.0F, 380, 6, 0.005F, 0.06F), "AUG");
        sks = writer.writeObject(specification -> new WeaponConfiguration(specification, 7.0F, 380, 6, 0.01F, 0.14F), "SKS");
        vss = writer.writeObject(specification -> new WeaponConfiguration(specification, 6.0F, 140, 2, 0.008F, 0.01F), "VSS");
        mk14 = writer.writeObject(specification -> new WeaponConfiguration(specification, 9.0F, 440, 7, 0.008F, 0.09F), "Mk-14");
        kar98k = writer.writeObject(specification -> new WeaponConfiguration(specification, 14.0F, 440, 7, 0.035F, 0.16F), "Kar98k");
        winchester = writer.writeObject(specification -> new WeaponConfiguration(specification, 10.0F, 340, 5, 0.02F, 0.15F), "Winchester");
        awm = writer.writeObject(specification -> new WeaponConfiguration(specification, 18.0F, 480, 8, 0.025F, 0.14F), "AWM");
        grenadeLauncher = writer.writeObject(specification -> new SimpleWeaponConfiguration(specification, 1.6f, 0.05F, 0.17F), "Grenade Launcher");
        rocketLauncher = writer.writeObject(specification -> new SimpleWeaponConfiguration(specification, 2.0f, 0.04F, 0.18F), "Rocket Launcher");
    }
}
