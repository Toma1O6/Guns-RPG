package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.ObjectType;

public class WeaponConfig extends ObjectType {

    public final GeneralSettings general;
    public final IWeaponConfiguration m1911;
    public final IWeaponConfiguration ump;
    public final IWeaponConfiguration sks;
    public final IWeaponConfiguration kar98k;
    public final IWeaponConfiguration s1897;
    public final IWeaponConfiguration crossbow;

    public WeaponConfig(IObjectSpec objectSpec) {
        super(objectSpec);
        IConfigWriter writer = objectSpec.getWriter();
        general = writer.writeObject(GeneralSettings::new, "Attachment settings");
        m1911 = writer.writeObject(sp -> WeaponConfiguration.withUpgrade(sp, 4.0F, 280, 3, 3.1F, 1.4F, 4, 3), "M1911");
        ump = writer.writeObject(sp -> WeaponConfiguration.withUpgrade(sp, 3.0F, 320, 5, 1.8F, 0.7F, 3, 2), "UMP-45");
        sks = writer.writeObject(sp -> WeaponConfiguration.withUpgrade(sp, 7.0F, 380, 6, 4.7F, 1.3F, 7, 6), "SKS");
        kar98k = writer.writeObject(sp -> WeaponConfiguration.withUpgrade(sp, 15.0F, 440, 7, 8.0F, 0.9F, 40, 25), "Kar98k");
        s1897 = writer.writeObject(sp -> WeaponConfiguration.withUpgrade(sp, 2.0F, 220, 1, 5.3F, 1.9F, 25, 15), "S1897");
        crossbow = writer.writeObject(sp -> WeaponConfiguration.basic(sp, 10.0F, 64, 3, 0.1F, 0.1F, 10), "Crossbow");
    }
}
