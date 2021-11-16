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

    public WeaponConfig(IObjectSpec objectSpecification) {
        super(objectSpecification);
        IConfigWriter writer = objectSpecification.getWriter();
        general = writer.writeObject(GeneralSettings::new, "Attachment settings");
        m1911 = writer.writeObject(specification -> IWeaponConfig.configured(specification, 4.0F, 280, 3), "M1911");
        r45 = writer.writeObject(specification -> IWeaponConfig.configured(specification, 7.0F, 320, 4), "R45");
        desertEagle = writer.writeObject(specification -> IWeaponConfig.configured(specification, 8.0F, 330, 5), "Desert Eagle");
        ump = writer.writeObject(specification -> IWeaponConfig.configured(specification, 3.0F, 320, 5), "UMP-45");
        thompson = writer.writeObject(specification -> IWeaponConfig.configured(specification, 5.0F, 320, 5), "Thompson");
        vector = writer.writeObject(specification -> IWeaponConfig.configured(specification, 3.0F, 280, 3), "Vector");
        crossbow = writer.writeObject(specification -> IWeaponConfig.configured(specification, 10.0F, 64, 3), "Crossbow");
        chukonu = writer.writeObject(specification -> IWeaponConfig.configured(specification, 6.0F, 98, 2), "Chu-Ko-Nu");
        s1897 = writer.writeObject(specification -> IWeaponConfig.configured(specification, 2.0F, 220, 1), "S1897");
        s686 = writer.writeObject(specification -> IWeaponConfig.configured(specification, 2.0F, 220, 1), "S686");
        s12k = writer.writeObject(specification -> IWeaponConfig.configured(specification, 2.0F, 220, 1), "S12k");
        akm = writer.writeObject(speicification -> IWeaponConfig.configured(speicification, 6.0F, 330, 4), "AKM");
        hk416 = writer.writeObject(specification -> IWeaponConfig.configured(specification, 5.0F, 380, 6), "HK 416");
        aug = writer.writeObject(specification -> IWeaponConfig.configured(specification, 5.0F, 380, 6), "AUG");
        sks = writer.writeObject(specification -> IWeaponConfig.configured(specification, 7.0F, 380, 6), "SKS");
        vss = writer.writeObject(specification -> IWeaponConfig.configured(specification, 6.0F, 140, 2), "VSS");
        mk14 = writer.writeObject(specification -> IWeaponConfig.configured(specification, 8.0F, 440, 7), "Mk-14");
        kar98k = writer.writeObject(specification -> IWeaponConfig.configured(specification, 15.0F, 440, 7), "Kar98k");
        winchester = writer.writeObject(specification -> IWeaponConfig.configured(specification, 10.0F, 340, 5), "Winchester");
        awm = writer.writeObject(specification -> IWeaponConfig.configured(specification, 18.0F, 480, 8), "AWM");
    }
}
